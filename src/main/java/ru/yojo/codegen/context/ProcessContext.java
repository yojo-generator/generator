package ru.yojo.codegen.context;

import ru.yojo.codegen.domain.lombok.LombokProperties;

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
     * Output Directory
     */
    private String outputDirectory;
    /**
     * Package, which will write in each generated class
     * specify package like: com.example.myproject
     */
    private String packageLocation;
    /**
     * Properties for lombok {@link LombokProperties}
     */
    private LombokProperties lombokProperties;

    /**
     * Replaced filePath
     */
    private String outputDirectoryName;

    /**
     * Path to write file
     */
    private String pathToWrite;

    /**
     * Package of messages, which will write in each generated class
     */
    private String messagePackage;

    /**
     * Package of schemas, which will write in each generated class
     */
    private String commonPackage;

    /**
     * Prepared messages map, from components.messages
     */
    private Map<String, Object> messagesMap;

    /**
     * Prepared messages map, from components.schemas
     */
    private Map<String, Object> schemasMap;

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

    public void setPackageLocation(String packageLocation) {
        this.packageLocation = packageLocation;
    }

    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public String getOutputDirectoryName() {
        return outputDirectoryName;
    }

    public void setOutputDirectoryName(String outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public String getPathToWrite() {
        return pathToWrite;
    }

    public void setPathToWrite(String pathToWrite) {
        new File(pathToWrite).mkdirs();
        this.pathToWrite = pathToWrite;
    }

    public String getMessagePackage() {
        return messagePackage;
    }

    public String getCommonPackage() {
        return commonPackage;
    }

    public void preparePackages() {
        messagePackage = getPackage(packageLocation, outputDirectoryName, MESSAGE_PACKAGE_IMPORT);
        commonPackage = getPackage(packageLocation, outputDirectoryName, COMMON_PACKAGE_IMPORT);
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
}
