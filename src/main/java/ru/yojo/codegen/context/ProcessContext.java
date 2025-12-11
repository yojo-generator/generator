package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.mapper.Helper;

import java.io.File;
import java.util.Map;

/**
 * Holds contextual data for a single AsyncAPI specification processing session.
 * Includes parsed content, configuration, and intermediate state (e.g., resolved schemas/messages).
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ProcessContext {
    private boolean experimental = false;
    /**
     * Constructs a new context with the raw parsed YAML content.
     *
     * @param content root AsyncAPI document as a Map (e.g., from SnakeYAML)
     */
    public ProcessContext(Map<String, Object> content) {
        this.content = content;
    }

    private String springBootVersion;

    /**
     * Entire parsed YAML content (root document).
     */
    private final Map<String, Object> content;

    /**
     * Absolute path to the input specification file (e.g., {@code /path/to/test.yaml}).
     */
    private String filePath;

    /**
     * Base output directory (without {@code /messages} or {@code /common} suffix).
     * Example: {@code "src/test/resources/example/testGenerate/"}.
     */
    private String outputDirectory;

    /**
     * Base Java package (without {@code .messages} or {@code .common} suffix).
     * Example: {@code "example.testGenerate"}.
     */
    private String packageLocation;

    /**
     * Lombok configuration effective for this generation run.
     */
    private LombokProperties lombokProperties;

    /**
     * Final output directory for generated files (e.g., {@code ".../testGenerate/common/"}).
     * <p>
     * Must be set explicitly — NOT derived from {@link #filePath} or {@link #outputDirectory}.
     */
    private String pathToWrite;

    /**
     * Full package for generated message classes (e.g., {@code "example.testGenerate.messages;"}).
     * <p>
     * If not set explicitly, defaults to {@code packageLocation + ".messages;"}.
     */
    private String messagePackage;

    /**
     * Full package for generated schema (DTO/enums) classes (e.g., {@code "example.testGenerate.common;"}).
     * <p>
     * If not set explicitly, defaults to {@code packageLocation + ".common;"}.
     */
    private String commonPackage;

    /**
     * Whether {@code @JsonPropertyDescription} should be generated.
     */
    private boolean jsonPropertyDescription;

    /**
     * Map of all message definitions from {@code components.messages}.
     */
    private Map<String, Object> messagesMap;

    /**
     * Map of all schema definitions from {@code components.schemas}.
     */
    private Map<String, Object> schemasMap;

    /**
     * Shared helper for cross-mapper state (e.g., inner schemas, removal flags).
     */
    private Helper helper = new Helper();

    public boolean isExperimental() { return experimental; }
    public void setExperimental(boolean experimental) { this.experimental = experimental; }
    /**
     * Returns the shared helper instance.
     *
     * @return non-null {@link Helper}
     */
    public Helper getHelper() {
        return helper;
    }

    /**
     * Sets the shared helper instance.
     *
     * @param helper helper to use (must not be null)
     */
    public void setHelper(Helper helper) {
        this.helper = helper;
    }

    /**
     * Returns the Spring Boot version string (e.g., {@code "3.x.x"}).
     * Used to select correct validation annotation packages (jakarta vs javax).
     *
     * @return Spring Boot version or {@code null} if unspecified
     */
    public String getSpringBootVersion() {
        return springBootVersion;
    }

    /**
     * Sets the Spring Boot version.
     *
     * @param springBootVersion version string (e.g., {@code "3.x.x"})
     */
    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    /**
     * Returns the full parsed AsyncAPI document.
     *
     * @return root YAML content as a {@code Map}
     */
    public Map<String, Object> getContent() {
        return content;
    }

    /**
     * Returns the absolute path to the input specification file.
     *
     * @return file path or {@code null} if not set
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the absolute path to the input specification file.
     *
     * @param filePath absolute path to YAML file
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the base output directory (without subdirectories like {@code /messages}).
     *
     * @return base output path
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the base output directory.
     *
     * @param outputDirectory directory path (e.g., {@code "src/gen/java/"})
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Returns the base Java package (without {@code .messages} or {@code .common}).
     *
     * @return package (e.g., {@code "com.example.api"})
     */
    public String getPackageLocation() {
        return packageLocation;
    }

    /**
     * Sets the base Java package.
     *
     * @param packageLocation base package name
     */
    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    /**
     * Returns the effective Lombok configuration.
     *
     * @return lombok properties
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Sets the Lombok configuration to use for generated classes.
     *
     * @param lombokProperties lombok config
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    /**
     * Returns the final output directory where files will be written (e.g., {@code ".../common/"}).
     *
     * @return absolute path to target directory
     */
    public String getPathToWrite() {
        return pathToWrite;
    }

    /**
     * Sets the final output directory and ensures it exists.
     * <p>
     * Does NOT append {@code /messages} or {@code /common} — the caller must provide the full path.
     *
     * @param pathToWrite absolute directory path; if {@code null}, no-op
     */
    public void setPathToWrite(String pathToWrite) {
        if (pathToWrite != null) {
            new File(pathToWrite).mkdirs();
            this.pathToWrite = pathToWrite;
        }
    }

    /**
     * Returns the package for message classes (e.g., {@code "com.example.api.messages;"}).
     * <p>
     * If unset, returns {@code packageLocation + ".messages;"}.
     *
     * @return message package with trailing semicolon
     */
    public String getMessagePackage() {
        if (messagePackage == null && packageLocation != null) {
            return packageLocation + ".messages;";
        }
        return messagePackage;
    }

    /**
     * Sets the package for message classes.
     *
     * @param messagePackage full package, e.g., {@code "com.example.api.messages;"}
     */
    public void setMessagePackage(String messagePackage) {
        this.messagePackage = messagePackage;
    }

    /**
     * Returns the package for schema (DTO/enums) classes (e.g., {@code "com.example.api.common;"}).
     * <p>
     * If unset, returns {@code packageLocation + ".common;"}.
     *
     * @return schema package with trailing semicolon
     */
    public String getCommonPackage() {
        if (commonPackage == null && packageLocation != null) {
            return packageLocation + ".common;";
        }
        return commonPackage;
    }

    /**
     * Sets the package for schema (DTO/enums) classes.
     *
     * @param commonPackage full package, e.g., {@code "com.example.api.common;"}
     */
    public void setCommonPackage(String commonPackage) {
        this.commonPackage = commonPackage;
    }

    /**
     * Enables or disables {@code @JsonPropertyDescription} generation.
     *
     * @param jsonPropertyDescription {@code true} to generate annotation
     */
    public void setJsonPropertyDescription(boolean jsonPropertyDescription) {
        this.jsonPropertyDescription = jsonPropertyDescription;
    }

    /**
     * Returns whether {@code @JsonPropertyDescription} should be generated.
     *
     * @return {@code true} if enabled
     */
    public boolean isJsonPropertyDescription() {
        return jsonPropertyDescription;
    }

    /**
     * Returns the map of all message definitions (from {@code components.messages}).
     *
     * @return messages map (schema name → definition)
     */
    public Map<String, Object> getMessagesMap() {
        return messagesMap;
    }

    /**
     * Sets the map of message definitions.
     *
     * @param messagesMap messages map
     */
    public void setMessagesMap(Map<String, Object> messagesMap) {
        this.messagesMap = messagesMap;
    }

    /**
     * Returns the map of all schema definitions (from {@code components.schemas}).
     *
     * @return schemas map (schema name → definition)
     */
    public Map<String, Object> getSchemasMap() {
        return schemasMap;
    }

    /**
     * Sets the map of schema definitions.
     *
     * @param schemasMap schemas map
     */
    public void setSchemasMap(Map<String, Object> schemasMap) {
        this.schemasMap = schemasMap;
    }
}