package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.context.SpecificationProperties;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;
import ru.yojo.codegen.util.MapperUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.mapper.AbstractMapper.fillMessageFromChannel;
import static ru.yojo.codegen.util.LogUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * AsyncAPI-to-Java DTO code generator YOJO.
 * <p>
 * Converts AsyncAPI 2.0/3.0 specification files (YAML) into:
 * <ul>
 *   <li>Message DTOs (under {@code .messages} package)</li>
 *   <li>Schema classes: DTOs, enums, interfaces (under {@code .common} package)</li>
 * </ul>
 * Supports:
 * <ul>
 *   <li>Full {@code $ref} resolution (including external files)</li>
 *   <li>Polymorphism ({@code oneOf}, {@code allOf}, {@code anyOf})</li>
 *   <li>Lombok, validation annotations (jakarta/javax), collection realizations</li>
 *   <li>Custom packages via {@code pathForGenerateMessage}</li>
 *   <li>AsyncAPI 3.0 {@code operations} + {@code channels} model</li>
 * </ul>
 * <p>
 * Entry point: {@link #generateAll(YojoContext)}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@Component
public class YojoGenerator {

    /**
     * Initializes and prints startup banner.
     */
    public YojoGenerator() {
        printLogo();
    }

    /**
     * Main generation entry point.
     * <p>
     * Iterates over all specification definitions in {@code yojoContext} and generates Java code for each.
     *
     * @param yojoContext top-level generation configuration
     * @throws IOException              if any I/O error occurs during file loading or writing
     * @throws IllegalArgumentException if no specifications are configured
     */
    public void generateAll(YojoContext yojoContext) throws IOException {
        if (yojoContext.getSpecificationProperties() == null || yojoContext.getSpecificationProperties().isEmpty()) {
            throw new IllegalArgumentException("specificationProperties is required.");
        }
        for (SpecificationProperties spec : yojoContext.getSpecificationProperties()) {
            validate(spec);
            processSpecification(spec, yojoContext);
        }
    }

    /**
     * Validates that a specification definition contains all required fields.
     *
     * @param spec specification to validate
     * @throws IllegalArgumentException if any required field is missing
     */
    private void validate(SpecificationProperties spec) {
        if (spec.getSpecName() == null || spec.getSpecName().trim().isEmpty()) {
            throw new IllegalArgumentException("specName (e.g. 'test.yaml') is required.");
        }
        if (spec.getInputDirectory() == null || spec.getInputDirectory().trim().isEmpty()) {
            throw new IllegalArgumentException("inputDirectory required for spec: " + spec.getSpecName());
        }
        if (spec.getOutputDirectory() == null || spec.getOutputDirectory().trim().isEmpty()) {
            throw new IllegalArgumentException("outputDirectory required for spec: " + spec.getSpecName());
        }
        if (spec.getPackageLocation() == null || spec.getPackageLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("packageLocation required for spec: " + spec.getSpecName());
        }
    }

    /**
     * Processes a single AsyncAPI specification file.
     *
     * @param spec        specification definition
     * @param yojoContext shared context (Lombok, Spring Boot version, global config)
     * @throws IOException if spec file not found or cannot be read
     */
    private void processSpecification(SpecificationProperties spec, YojoContext yojoContext) throws IOException {
        SchemaMapper schemaMapper = new SchemaMapper();
        MessageMapper messageMapper = new MessageMapper(schemaMapper);
        Path inputDir = Paths.get(spec.getInputDirectory()).toAbsolutePath().normalize();
        String specFileName = spec.getSpecName().trim();
        Path specFilePath = inputDir.resolve(specFileName);
        if (!Files.exists(specFilePath)) {
            throw new IllegalArgumentException("Spec file not found: " + specFilePath);
        }

        // Load root spec
        String rootContent = Files.readString(specFilePath, StandardCharsets.UTF_8);
        Map<String, Object> rootDoc = new Yaml().load(rootContent);
        String firstLine = rootContent.lines().findFirst().orElse("");
        if (!firstLine.startsWith("asyncapi:") && !firstLine.startsWith("openapi:")) {
            throw new IllegalArgumentException("Not a root spec: " + specFilePath);
        }

        // Collect all schemas/messages from root + $ref files (recursively)
        Map<String, Object> globalSchemas = new LinkedHashMap<>();
        Map<String, Object> globalMessages = new LinkedHashMap<>();
        Set<String> visitedRefs = new HashSet<>();
        collectSchemasAndMessages(rootDoc, globalSchemas, globalMessages);
        collectExternalRefs(rootDoc, inputDir, globalSchemas, globalMessages, visitedRefs);

        // Inject 'name' and 'package' for $ref resolution
        rootContent = getContent(rootContent, spec.getPackageLocation());

        // Prepare generation context
        ProcessContext ctx = new ProcessContext(new Yaml().load(rootContent));
        ctx.setFilePath(specFilePath.toString());
        ctx.setPackageLocation(spec.getPackageLocation());
        ctx.setLombokProperties(yojoContext.getLombokProperties());
        ctx.setSpringBootVersion(yojoContext.getSpringBootVersion());
        ctx.setOutputDirectory(spec.getOutputDirectory());
        ctx.setPathToWrite(spec.getOutputDirectory());
        ctx.setMessagePackage(spec.getPackageLocation() + ".messages;");
        ctx.setCommonPackage(spec.getPackageLocation() + ".common;");
        ctx.setSchemasMap(globalSchemas);
        ctx.setMessagesMap(globalMessages);

        process(ctx, schemaMapper, messageMapper);
    }

    // ————————————————————————————————————————
    // Helper methods for $ref collection
    // ————————————————————————————————————————

    /**
     * Extracts schemas and messages from the {@code components} section of a spec document.
     *
     * @param doc      source document
     * @param schemas  target map for schemas
     * @param messages target map for messages
     */
    private void collectSchemasAndMessages(Map<String, Object> doc,
                                           Map<String, Object> schemas,
                                           Map<String, Object> messages) {
        if (doc == null) return;
        Map<String, Object> components = castObjectToMap(doc.get("components"));
        if (components != null) {
            Map<String, Object> compsSchemas = castObjectToMap(components.get("schemas"));
            Map<String, Object> compsMessages = castObjectToMap(components.get("messages"));
            if (compsSchemas != null) schemas.putAll(compsSchemas);
            if (compsMessages != null) messages.putAll(compsMessages);
        }
    }

    /**
     * Recursively walks the YAML AST and collects schemas/messages from external {@code $ref} files.
     *
     * @param node     current AST node
     * @param baseDir  base directory for relative path resolution
     * @param schemas  target schema map
     * @param messages target message map
     * @param visited  set of already visited file paths (to prevent cycles)
     */
    private void collectExternalRefs(Object node,
                                     Path baseDir,
                                     Map<String, Object> schemas,
                                     Map<String, Object> messages,
                                     Set<String> visited) {
        if (node instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) node;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if ("$ref".equals(key) && value instanceof String) {
                    String ref = (String) value;
                    if (ref.startsWith("./") || ref.startsWith("../")) {
                        int hash = ref.indexOf('#');
                        if (hash > 0) {
                            String filePath = ref.substring(0, hash);
                            if (visited.add(filePath)) {
                                try {
                                    Path absPath = baseDir.resolve(filePath).normalize();
                                    if (absPath.startsWith(baseDir) && Files.exists(absPath)) {
                                        Map<String, Object> externalDoc = new Yaml().load(
                                                Files.readString(absPath, StandardCharsets.UTF_8));
                                        collectSchemasAndMessages(externalDoc, schemas, messages);
                                        collectExternalRefs(externalDoc, baseDir, schemas, messages, visited);
                                    }
                                } catch (Exception e) {
                                    System.err.println("⚠️ Skip $ref: " + ref + " → " + e.getMessage());
                                }
                            }
                        }
                    }
                } else {
                    collectExternalRefs(value, baseDir, schemas, messages, visited);
                }
            }
        } else if (node instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) node;
            for (Object item : list) {
                collectExternalRefs(item, baseDir, schemas, messages, visited);
            }
        }
        // else: primitive — ignore
    }

    // ————————————————————————————————————————
    // Core processing
    // ————————————————————————————————————————

    /**
     * Detects if the document is AsyncAPI 3.x.
     *
     * @param doc parsed AsyncAPI document
     * @return {@code true} if asyncapi version starts with "3."
     */
    private boolean isAsyncApiV3(Map<String, Object> doc) {
        Object asyncapi = doc.get("asyncapi");
        if (asyncapi instanceof String versionStr) {
            return versionStr.startsWith("3.");
        }
        return false;
    }

    /**
     * Main processing pipeline: resolves messages (if empty), then generates schemas and messages.
     *
     * @param ctx           generation context
     * @param schemaMapper  schema mapper
     * @param messageMapper message mapper
     */
    private void process(ProcessContext ctx, SchemaMapper schemaMapper, MessageMapper messageMapper) {
        Map<String, Object> messagesMap = ctx.getMessagesMap();
        Map<String, Object> schemasMap = ctx.getSchemasMap();

        // Populate messages if none defined (e.g., AsyncAPI 3.0 w/o explicit components.messages)
        if (messagesMap.isEmpty()) {
            Set<String> dummyExclude = new HashSet<>(); // unused
            if (isAsyncApiV3(ctx.getContent())) {
                fillMessagesFromOperations(ctx.getContent(), messagesMap);
            } else {
                fillMessagesByChannel(ctx.getContent(), messagesMap, dummyExclude);
            }
        }

        ctx.setMessagesMap(messagesMap);
        ctx.setSchemasMap(schemasMap);
        processMessages(ctx, messageMapper);
        processSchemas(ctx, schemaMapper);
        System.out.println(LOG_FINISH);
    }

    /**
     * Populates {@code messagesMap} from AsyncAPI 3.0 {@code operations} and {@code channels}.
     * <p>
     * Supports:
     * <ul>
     *   <li>Direct payload definitions</li>
     *   <li>{@code $ref} to {@code components.messages}</li>
     *   <li>Gitter-style {@code payload: { schema: { ... } }}</li>
     * </ul>
     *
     * @param doc         source AsyncAPI document
     * @param messagesMap output map to fill
     */
    private void fillMessagesFromOperations(Map<String, Object> doc, Map<String, Object> messagesMap) {
        Map<String, Object> operations = castObjectToMap(doc.get("operations"));
        Map<String, Object> channels = castObjectToMap(doc.get("channels"));
        Map<String, Object> components = castObjectToMap(doc.get("components"));
        Map<String, Object> compMessages = components != null ? castObjectToMap(components.get("messages")) : Collections.emptyMap();

        if (operations == null) return;

        operations.forEach((opId, opObj) -> {
            Map<String, Object> op = castObjectToMap(opObj);
            // Resolve channel
            Map<String, Object> channelRef = castObjectToMap(op.get("channel"));
            String channelRefStr = getStringValueIfExistOrElseNull("$ref", channelRef);
            if (channelRefStr == null || !channelRefStr.startsWith("#/channels/")) return;

            String channelId = channelRefStr.substring("#/channels/".length());
            Map<String, Object> channel = castObjectToMap(channels.get(channelId));
            if (channel == null) return;

            // Resolve messages list
            List<Object> msgRefObjs = castObjectToListObjects(op.get("messages"));
            for (Object msgRefObj : msgRefObjs) {
                Map<String, Object> ref = castObjectToMap(msgRefObj);
                String refStr = getStringValueIfExistOrElseNull("$ref", ref);
                if (refStr == null) continue;

                // Format: "#/channels/{channelId}/messages/{msgKey}"
                if (refStr.startsWith("#/channels/")) {
                    String[] parts = refStr.split("/");
                    if (parts.length != 5 || !"messages".equals(parts[3])) continue;
                    String msgKey = parts[4];

                    Map<String, Object> channelMsgs = castObjectToMap(channel.get("messages"));
                    Map<String, Object> msgDef = castObjectToMap(channelMsgs.get(msgKey));
                    if (msgDef == null) {
                        System.err.println("⚠️ Channel message not found: " + refStr);
                        continue;
                    }

                    // Resolve first-level $ref (e.g. from channel → components.messages)
                    if (getStringValueIfExistOrElseNull("$ref", msgDef) != null) {
                        String compRef = msgDef.get("$ref").toString();
                        if (compRef.startsWith("#/components/messages/")) {
                            String compName = compRef.substring("#/components/messages/".length());
                            msgDef = castObjectToMap(compMessages.get(compName));
                            if (msgDef == null) {
                                System.err.println("⚠️ Component message not found: " + compName);
                                continue;
                            }
                        }
                    }

                    Object payload = msgDef.get("payload");
                    if (payload == null) {
                        System.err.println("⚠️ Payload not found in message: " + msgKey);
                        continue;
                    }

                    // Support gitter-style: payload: { schemaFormat: ..., schema: { ... } }
                    Map<String, Object> payloadMap = castObjectToMap(payload);
                    if (payloadMap.containsKey("schema")) {
                        payload = payloadMap.get("schema");
                    }

                    // Use msgKey as message name — consistent with v2 behaviour
                    String msgName = capitalize(msgKey);
                    messagesMap.put(msgName, Map.of("payload", payload));
                }
            }
        });
    }

    /**
     * Populates {@code messagesMap} from AsyncAPI 2.0 {@code channels}/{@code publish}/{@code subscribe}.
     *
     * @param allContent     source document
     * @param messagesMap    output map to fill
     * @param excludeSchemas unused (legacy)
     */
    private void fillMessagesByChannel(Map<String, Object> allContent, Map<String, Object> messagesMap, Set<String> excludeSchemas) {
        Map<String, Object> channelsMap = castObjectToMap(allContent.get(CHANNELS));
        channelsMap.entrySet().forEach(entry -> {
            String channelName = entry.getKey();
            Map<String, Object> subscribeMap = castObjectToMap(entry.getValue()).entrySet().stream()
                    .filter(e -> SUBSCRIBE.equals(e.getKey()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(MapperUtil::castObjectToMap)
                    .orElse(Collections.emptyMap());
            Map<String, Object> publishMap = castObjectToMap(entry.getValue()).entrySet().stream()
                    .filter(e -> PUBLISH.equals(e.getKey()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(MapperUtil::castObjectToMap)
                    .orElse(Collections.emptyMap());
            if (!subscribeMap.isEmpty()) {
                fillMessageFromChannel(allContent, messagesMap, excludeSchemas, subscribeMap, channelName, SUBSCRIBE);
            }
            if (!publishMap.isEmpty()) {
                fillMessageFromChannel(allContent, messagesMap, excludeSchemas, publishMap, channelName, PUBLISH);
            }
        });
    }

    /**
     * Generates and writes all schema classes (DTOs, enums, interfaces).
     *
     * @param ctx          generation context
     * @param schemaMapper mapper
     */
    private void processSchemas(ProcessContext ctx, SchemaMapper schemaMapper) {
        System.out.println(LOG_DELIMETER);
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(ctx);
        if (!schemaList.isEmpty()) {
            System.out.println();
            System.out.println("START WRITING JAVA CLASS FROM SCHEMAS:");
            schemaList.forEach(schema -> System.out.println(schema.getSchemaName()));
            System.out.println();
            writeSchemas(ctx, schemaMapper);
            System.out.println(LOG_DELIMETER + ANSI_RESET);
            System.out.println();
        }
    }

    /**
     * Generates and writes all message DTOs.
     *
     * @param ctx           generation context
     * @param messageMapper mapper
     */
    private void processMessages(ProcessContext ctx, MessageMapper messageMapper) {
        System.out.println(ANSI_CYAN + LOG_DELIMETER);
        List<Message> messageList = messageMapper.mapMessagesToObjects(ctx);
        System.out.println();
        System.out.println("START WRITING JAVA CLASS FROM MESSAGES:");
        messageList.forEach(message -> System.out.println(message.getMessageName()));
        System.out.println();
        writeMessages(ctx, messageMapper);
        System.out.println(LOG_DELIMETER);
        System.out.println();
    }

    /**
     * Writes all message DTOs to the filesystem.
     *
     * @param ctx           generation context
     * @param messageMapper mapper
     */
    private void writeMessages(ProcessContext ctx, MessageMapper messageMapper) {
        List<Message> messageList = messageMapper.mapMessagesToObjects(ctx);
        String baseOutputPath = ctx.getPathToWrite();
        final String defaultMessagesPath = baseOutputPath.endsWith("/")
                ? baseOutputPath + "messages/"
                : baseOutputPath + "/messages/";
        messageList.forEach(message -> {
            String messagesPath = defaultMessagesPath;
            String pathForGenerateMessage = message.getPathForGenerateMessage();
            if (pathForGenerateMessage != null) {
                String customPath = pathForGenerateMessage.replace('.', '/');
                messagesPath = baseOutputPath.endsWith("/")
                        ? baseOutputPath + customPath + "/"
                        : baseOutputPath + "/" + customPath + "/";
                message.setMessagePackageName(pathForGenerateMessage + ";");
            }
            writeFile(messagesPath, message.getMessageName(), message.toWrite());
        });
    }

    /**
     * Writes all schema classes to the filesystem.
     *
     * @param ctx          generation context
     * @param schemaMapper mapper
     */
    private void writeSchemas(ProcessContext ctx, SchemaMapper schemaMapper) {
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(ctx);
        String baseOutputPath = ctx.getPathToWrite();
        final String schemasPath = baseOutputPath.endsWith("/")
                ? baseOutputPath + "common/"
                : baseOutputPath + "/common/";
        schemaList.forEach(schema -> {
            writeFile(schemasPath, schema.getSchemaName(), schema.toWrite());
        });
    }

    /**
     * Writes generated Java source code to a file.
     *
     * @param dirPath  directory path
     * @param fileName class name (without {@code .java} suffix)
     * @param content  full Java source
     * @throws RuntimeException if I/O error occurs
     */
    private static void writeFile(String dirPath, String fileName, String content) {
        new File(dirPath).mkdirs();
        File file = new File(dirPath + fileName + ".java");
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            pw.write(content);
            pw.flush();
            System.out.println(" Written: " + fileName + ".java → " + dirPath);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write: " + file, ex);
        }
    }

    /**
     * Preprocesses YAML content to inject {@code name} and {@code package} for external {@code $ref} resolution.
     * <p>
     * Converts:
     * <pre>
     *   $ref: './separated/primitives.yaml#/components/schemas/Primitives'
     * </pre>
     * to:
     * <pre>
     *   name: Primitives
     *   package: com.example.common
     *   $ref: './separated/primitives.yaml#/components/schemas/Primitives'
     * </pre>
     *
     * @param content         raw YAML
     * @param packageLocation base package (e.g., {@code com.example})
     * @return preprocessed YAML
     */
    private static String getContent(String content, String packageLocation) {
        String regex = "\\s*\\$ref:\\s*['\"](\\./[^'\"]*\\.yaml)(?:#/(.*))?['\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuilder modified = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            modified.append(content, lastEnd, matcher.start());
            String className = matcher.group(2);
            if (className != null) {
                className = className.substring(className.lastIndexOf('/') + 1);
            }
            // ALWAYS: packageLocation + ".common"
            String effectivePackage = packageLocation + ".common";
            String originalLine = matcher.group(0);
            String indent = originalLine.substring(0, originalLine.indexOf("$ref:"));
            String replaceName = String.format("%sname: %s", indent, className);
            String replacePackage = String.format("%spackage: %s", indent, effectivePackage);
            modified.append(replaceName).append("\n").append(replacePackage).append("\n");
            lastEnd = matcher.end();
        }
        modified.append(content.substring(lastEnd));
        return modified.toString();
    }
}