package ru.yojo.codegen.parser;

import org.yaml.snakeyaml.Yaml;

import ru.yojo.codegen.util.MapperUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;

/**
 * Facade for parsing AsyncAPI specification files (YAML).
 * Encapsulates reading YAML, resolving $ref, and collecting schemas/messages.
 *
 * <p>This class centralizes all specification parsing logic that was previously
 * scattered in {@code YojoGenerator}.</p>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SpecificationParser {

    private final String specFileName;
    private final Path inputDir;
    private final String packageLocation;

    private Map<String, Object> rootDoc;
    private Map<String, Object> globalSchemas;
    private Map<String, Object> globalMessages;

    /**
     * Creates a new parser for the given specification.
     *
     * @param specFileName   name of the spec file (e.g., "test.yaml")
     * @param inputDir       base directory containing the spec file
     * @param packageLocation target Java package (e.g., "com.example")
     */
    public SpecificationParser(String specFileName, Path inputDir, String packageLocation) {
        this.specFileName = specFileName.trim();
        this.inputDir = inputDir.toAbsolutePath().normalize();
        this.packageLocation = packageLocation;
        this.globalSchemas = new LinkedHashMap<>();
        this.globalMessages = new LinkedHashMap<>();
    }

    /**
     * Parses the specification and collects all schemas and messages.
     *
     * @return a {@link SpecificationModel} containing the parsed data
     * @throws IOException if the spec file cannot be read
     */
    public SpecificationModel parse() throws IOException {
        Path specFilePath = inputDir.resolve(specFileName);
        if (!Files.exists(specFilePath)) {
            throw new IllegalArgumentException("Spec file not found: " + specFilePath);
        }

        // Load root spec
        String rootContent = Files.readString(specFilePath, StandardCharsets.UTF_8);
        rootDoc = new Yaml().load(rootContent);

        String firstLine = rootContent.lines().findFirst().orElse("");
        if (!firstLine.startsWith("asyncapi:") && !firstLine.startsWith("openapi:")) {
            throw new IllegalArgumentException("Not a root spec: " + specFilePath);
        }

        // Collect all schemas/messages from root + $ref files (recursively)
        collectSchemasAndMessages(rootDoc, globalSchemas, globalMessages);
        Set<String> visitedRefs = new HashSet<>();
        collectExternalRefs(rootDoc, inputDir, globalSchemas, globalMessages, visitedRefs);

        // Inject 'name' and 'package' for $ref resolution
        rootContent = getContent(rootContent, packageLocation);
        rootDoc = new Yaml().load(rootContent);

        return new SpecificationModel(rootDoc, globalSchemas, globalMessages);
    }

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
        // else: ignore
    }

    /**
     * Gets the root document (useful for further processing).
     *
     * @return the parsed root document
     */
    public Map<String, Object> getRootDoc() {
        return rootDoc;
    }

    /**
     * Preprocesses YAML content to inject {@code name} and {@code package} for external {@code $ref} resolution.
     *
     * @param content         raw YAML content
     * @param packageLocation base package (e.g., {@code com.example})
     * @return preprocessed YAML content
     */
    public static String getContent(String content, String packageLocation) {
        String regex = "\\s*\\$ref:\\s*['\"](./[^'\"]*\\.yaml)(?:#/(.*))?['\"]";
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
