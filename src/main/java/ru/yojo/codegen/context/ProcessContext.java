package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.mapper.Helper;

import java.io.File;
import java.util.Map;

import static ru.yojo.codegen.constants.Dictionary.COMMON_PACKAGE_IMPORT;
import static ru.yojo.codegen.constants.Dictionary.MESSAGE_PACKAGE_IMPORT;
import static ru.yojo.codegen.util.MapperUtil.getPackage;

public class ProcessContext {

    public ProcessContext(Map<String, Object> content) {
        this.content = content;
    }

    private String springBootVersion;

    /**
     * All content from YAML-file
     */
    private final Map<String, Object> content;

    /**
     * Path to file
     */
    private String filePath;

    /**
     * Output directory (e.g. "src/test/resources/example/testGenerate/")
     */
    private String outputDirectory;

    /**
     * Base package location (e.g. "example.testGenerate")
     */
    private String packageLocation;

    /**
     * Lombok properties
     */
    private LombokProperties lombokProperties;

    /**
     * ⚠️ DEPRECATED — DO NOT USE in new logic.
     * Used only for legacy compatibility; ignored in new generation flow.
     */
    @Deprecated
    private String outputDirectoryName;

    /**
     * Final directory to write files (e.g. "src/test/resources/example/testGenerate/common/")
     * Must be set explicitly — NOT derived from fileName.
     */
    private String pathToWrite;

    /**
     * Full package for messages (e.g. "example.testGenerate.messages;")
     * Must be set explicitly or derived from packageLocation + ".messages;"
     */
    private String messagePackage;

    /**
     * Full package for schemas (e.g. "example.testGenerate.common;")
     * Must be set explicitly or derived from packageLocation + ".common;"
     */
    private String commonPackage;

    private boolean jsonPropertyDescription;

    /**
     * Prepared messages map, from components.messages
     */
    private Map<String, Object> messagesMap;

    /**
     * Prepared schemas map, from components.schemas
     */
    private Map<String, Object> schemasMap;

    private Helper helper = new Helper();

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }
    // ---------------------------------------------
    // Getters & Setters
    // ---------------------------------------------

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getPackageLocation() {
        return packageLocation;
    }

    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    @Deprecated
    public String getOutputDirectoryName() {
        return outputDirectoryName;
    }

    @Deprecated
    public void setOutputDirectoryName(String outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public String getPathToWrite() {
        return pathToWrite;
    }

    /**
     * Sets pathToWrite and ensures directory exists.
     * Does NOT append outputDirectoryName — caller must provide full path.
     */
    public void setPathToWrite(String pathToWrite) {
        if (pathToWrite != null) {
            new File(pathToWrite).mkdirs();
            this.pathToWrite = pathToWrite;
        }
    }

    public String getMessagePackage() {
        if (messagePackage == null && packageLocation != null) {
            return packageLocation + "." + "messages;";
        }
        return messagePackage;
    }

    public void setMessagePackage(String messagePackage) {
        this.messagePackage = messagePackage;
    }

    public String getCommonPackage() {
        if (commonPackage == null && packageLocation != null) {
            return packageLocation + "." + "common;";
        }
        return commonPackage;
    }

    public void setCommonPackage(String commonPackage) {
        this.commonPackage = commonPackage;
    }

    public void setJsonPropertyDescription(boolean jsonPropertyDescription) {
        this.jsonPropertyDescription = jsonPropertyDescription;
    }

    public boolean isJsonPropertyDescription() {
        return jsonPropertyDescription;
    }

    public Map<String, Object> getMessagesMap() {
        return messagesMap;
    }

    public void setMessagesMap(Map<String, Object> messagesMap) {
        this.messagesMap = messagesMap;
    }

    public Map<String, Object> getSchemasMap() {
        return schemasMap;
    }

    public void setSchemasMap(Map<String, Object> schemasMap) {
        this.schemasMap = schemasMap;
    }

    /**
     * ⚠️ DEPRECATED — DO NOT USE.
     * This method injects outputDirectoryName into package string, causing unwanted "test." in imports.
     */
    @Deprecated
    public void preparePackages() {
        // Legacy fallback only — do not call in new flow.
        if (outputDirectoryName != null && !outputDirectoryName.isEmpty()) {
            messagePackage = getPackage(packageLocation, outputDirectoryName, MESSAGE_PACKAGE_IMPORT);
            commonPackage = getPackage(packageLocation, outputDirectoryName, COMMON_PACKAGE_IMPORT);
        } else {
            messagePackage = packageLocation + "." + "messages;";
            commonPackage = packageLocation + "." + "common;";
        }
    }
}