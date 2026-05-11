package ru.yojo.codegen.parser;

import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.util.Logger;
import ru.yojo.codegen.util.MapperUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.yojo.codegen.constants.Dictionary.CHANNELS;
import static ru.yojo.codegen.constants.Dictionary.PUBLISH;
import static ru.yojo.codegen.constants.Dictionary.SUBSCRIBE;
import static ru.yojo.codegen.mapper.AbstractMapper.fillMessageFromChannel;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.castObjectToListObjects;
import static ru.yojo.codegen.util.MapperUtil.capitalize;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Parses an AsyncAPI specification file and extracts all schemas, messages, and metadata
 * needed for Java DTO generation.
 * <p>
 * Handles:
 * <ul>
 *   <li>YAML loading and validation</li>
 *   <li>{@code components.schemas} and {@code components.messages} extraction</li>
 *   <li>Recursive external {@code $ref} resolution</li>
 *   <li>Message resolution from channels (AsyncAPI 2.x) or operations (AsyncAPI 3.x)</li>
 *   <li>YAML preprocessing for inline name/package injection</li>
 * </ul>
 */
public class AsyncAPIParser {

    private static final Logger LOG = new Logger(AsyncAPIParser.class);

    /**
     * Parses an AsyncAPI specification file and returns a structured result.
     *
     * @param specFilePath     absolute path to the spec file
     * @param baseDir          base directory for resolving relative {@code $ref} paths
     * @param packageLocation  base package (e.g., {@code "com.example"})
     * @return structured parse result
     * @throws IOException          if the spec file cannot be read
     * @throws IllegalArgumentException if the file is not a root AsyncAPI/OpenAPI spec
     */
    public ParseResult parse(Path specFilePath, Path baseDir, String packageLocation) throws IOException {
        // Read file
        String rootContent = Files.readString(specFilePath, StandardCharsets.UTF_8);
        Map<String, Object> rootDoc = new Yaml().load(rootContent);

        // Validate it is a root spec
        String firstLine = rootContent.lines().findFirst().orElse("");
        if (!firstLine.startsWith("asyncapi:") && !firstLine.startsWith("openapi:")) {
            throw new IllegalArgumentException("Not a root spec: " + specFilePath);
        }

        // Collect schemas and messages from components
        Map<String, Object> schemas = new LinkedHashMap<>();
        Map<String, Object> messages = new LinkedHashMap<>();
        collectSchemasAndMessages(rootDoc, schemas, messages);

        // Recursively collect from external $ref files
        collectExternalRefs(rootDoc, baseDir, schemas, messages, new HashSet<>());

        // Preprocess: inject 'name' and 'package' for $ref resolution
        String processedContent = getContent(rootContent, packageLocation);

        // Determine AsyncAPI version
        boolean isV3 = isAsyncApiV3(rootDoc);

        // Resolve messages from channels/operations if none defined in components
        if (messages.isEmpty()) {
            if (isV3) {
                fillMessagesFromOperations(rootDoc, messages);
            } else {
                fillMessagesByChannel(rootDoc, messages, new HashSet<>());
            }
        }

        return new ParseResult(rootDoc, processedContent, schemas, messages, isV3);
    }

    // ============================================================
    // Schema / Message collection helpers
    // ============================================================

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
                                    LOG.error("⚠️ Skip $ref: " + ref + " → " + e.getMessage(), e);
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
    }

    // ============================================================
    // AsyncAPI version detection
    // ============================================================

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

    // ============================================================
    // Message resolution (AsyncAPI 3.x)
    // ============================================================

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
        Map<String, Object> compMessages = components != null
                ? castObjectToMap(components.get("messages"))
                : Collections.emptyMap();

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
                        LOG.error("⚠️ Channel message not found: " + refStr);
                        continue;
                    }

                    // Resolve first-level $ref (e.g. from channel → components.messages)
                    if (getStringValueIfExistOrElseNull("$ref", msgDef) != null) {
                        String compRef = msgDef.get("$ref").toString();
                        if (compRef.startsWith("#/components/messages/")) {
                            String compName = compRef.substring("#/components/messages/".length());
                            msgDef = castObjectToMap(compMessages.get(compName));
                            if (msgDef == null) {
                                LOG.error("⚠️ Component message not found: " + compName);
                                continue;
                            }
                        }
                    }

                    Object payload = msgDef.get("payload");
                    if (payload == null) {
                        LOG.error("⚠️ Payload not found in message: " + msgKey);
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

    // ============================================================
    // Message resolution (AsyncAPI 2.x)
    // ============================================================

    /**
     * Populates {@code messagesMap} from AsyncAPI 2.0 {@code channels}/{@code publish}/{@code subscribe}.
     *
     * @param allContent     source document
     * @param messagesMap    output map to fill
     * @param excludeSchemas unused (legacy)
     */
    private void fillMessagesByChannel(Map<String, Object> allContent,
                                        Map<String, Object> messagesMap,
                                        Set<String> excludeSchemas) {
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
     * YAML preprocessing — injects {@code name} and {@code package} before external {@code $ref} targets
     * so that downstream mapping can resolve class names and packages.
     *
     * @param content         raw YAML content
     * @param packageLocation base package (e.g., {@code "com.example"})
     * @return preprocessed YAML with injected fields
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
