package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.context.YojoContext;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.mapper.AbstractMapper.fillMessageFromChannel;
import static ru.yojo.codegen.util.LogUtils.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * POJO Generator
 * <p>
 * To use generator in your project you need just inject bean Generator
 * <p>
 * Developed by Vladimir Morozkin
 * March 2023
 */
@SuppressWarnings("all")
@Component
public class YojoGenerator implements Generator {

    private final SchemaMapper schemaMapper;
    private final MessageMapper messageMapper;

    public YojoGenerator(SchemaMapper schemaMapper, MessageMapper messageMapper) {
        this.schemaMapper = schemaMapper;
        this.messageMapper = messageMapper;
        printLogo();
    }

    /**
     * Method for generating from all files in directory with spring boot version
     *
     * @param yojoContext Context with all properties
     */
    public void generateAll(YojoContext yojoContext) throws IOException {
        Files.walk(Paths.get(yojoContext.getDirectory()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(file -> {
                            String content;
                            try {
                                content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            String pathByDirectory = yojoContext.getDirectory().replaceAll("/", "\\\\");
                            String substring = file.getPath().substring(pathByDirectory.length());
                            // Find the index of the last occurrence of the backslash
                            int lastIndex = substring.lastIndexOf('\\');
                            String modifiedString = "";
                            if (lastIndex != -1) {
                                // Extract the substring up to the last backslash
                                modifiedString = substring.substring(0, lastIndex);

                                // Replace backslashes with forward slashes
                                modifiedString = modifiedString.replace('\\', '/');

                                // Remove the leading slash if it exists
                                if (modifiedString.startsWith("/")) {
                                    modifiedString = modifiedString.substring(1);
                                }
                            }
                            String packageLocation = modifiedString.isBlank() ?
                                    yojoContext.getPackageLocation() :
                                    String.join(".", yojoContext.getPackageLocation(), modifiedString.replace("/", "."));
                            content = getContent(content, packageLocation);

                            if (content.startsWith("asyncapi")) {
                                ProcessContext processContext = new ProcessContext(new Yaml().load(content));
                                processContext.setFilePath(file.getPath());
                                processContext.setOutputDirectory(yojoContext.getOutputDirectory() + modifiedString.replace('\\', '/'));
                                processContext.setPackageLocation(packageLocation);
                                processContext.setLombokProperties(yojoContext.getLombokProperties());
                                processContext.setSpringBootVersion(yojoContext.getSpringBootVersion());
                                processContext.setJsonPropertyDescription(yojoContext.isJsonPropertyDescription());
                                process(processContext);
                            }
                        }
                );
    }

    /**
     * Method for generating from all files in directory
     *
     * @param directory        - which directory use for search contracts
     * @param outputDirectory  - directory to output generated classes
     * @param packageLocation  - package, which will write in each generated class
     * @param lombokProperties - properies of lombok
     * @throws IOException
     */
    @Override
    public void generateAll(String directory,
                            String outputDirectory,
                            String packageLocation,
                            LombokProperties lombokProperties) throws IOException {
        Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(file -> {
                            String content;
                            try {
                                content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            String pathByDirectory = directory.replaceAll("/", "\\\\");
                            String substring = file.getPath().substring(pathByDirectory.length());
                            // Find the index of the last occurrence of the backslash
                            int lastIndex = substring.lastIndexOf('\\');
                            String modifiedString = "";
                            if (lastIndex != -1) {
                                // Extract the substring up to the last backslash
                                modifiedString = substring.substring(0, lastIndex);

                                // Replace backslashes with forward slashes
                                modifiedString = modifiedString.replace('\\', '/');

                                // Remove the leading slash if it exists
                                if (modifiedString.startsWith("/")) {
                                    modifiedString = modifiedString.substring(1);
                                }
                            }
                            String packageLoc = modifiedString.isBlank() ?
                                    packageLocation :
                                    String.join(".", packageLocation, modifiedString.replace("/", "."));
                            content = getContent(content, packageLocation);
                            if (content.startsWith("asyncapi") || content.startsWith("openapi")) {
                                ProcessContext processContext = new ProcessContext(new Yaml().load(content));
                                processContext.setFilePath(file.getPath());
                                processContext.setOutputDirectory(outputDirectory + modifiedString.replace('\\', '/'));
                                processContext.setPackageLocation(packageLoc);
                                processContext.setLombokProperties(lombokProperties);
                                processContext.setSpringBootVersion("2");

                                process(processContext);
                            }
                        }
                );
    }

    /**
     * Main method for generate POJO
     */
    @Override
    public void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties) {
        ProcessContext processContext;
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            processContext = new ProcessContext(new Yaml().load(fileInputStream));
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        processContext.setFilePath(filePath);
        processContext.setOutputDirectory(outputDirectory);
        processContext.setPackageLocation(packageLocation);
        processContext.setLombokProperties(lombokProperties);
        processContext.setSpringBootVersion("2");

        process(processContext);
    }

    /**
     * @param processContext Context with all properties
     */
    private void process(ProcessContext processContext) {
        String outputDirectoryName = new File(processContext.getFilePath()).getName().replaceAll("\\..*", "");
        if (!processContext.getOutputDirectory().endsWith("/")) {
            processContext.setOutputDirectory(processContext.getOutputDirectory() + DELIMITER);
        }
        processContext.setOutputDirectoryName(outputDirectoryName);
        processContext.setPathToWrite(processContext.getOutputDirectory() + outputDirectoryName);
        processContext.preparePackages();

        Map<String, Object> messagesMap =
                castObjectToMap(
                        castObjectToMap(
                                castObjectToMap(processContext.getContent().get("components"))).get("messages"));

        Map<String, Object> schemasMap =
                castObjectToMap(
                        castObjectToMap(
                                castObjectToMap(processContext.getContent().get("components"))).get("schemas"));

        Set<String> excludeSchemas = new HashSet<>();
        if (messagesMap.isEmpty()) {
            fillMessagesByChannel(processContext.getContent(), messagesMap, excludeSchemas);
        }
        excludeSchemas.forEach(sc -> schemasMap.remove(sc));
        //analyzeSchemas(filePath, schemasMap);

        processContext.setMessagesMap(messagesMap);
        processContext.setSchemasMap(schemasMap);

        processMessages(processContext);
        processSchemas(processContext);

        System.out.println(LOG_FINISH);
    }

    /**
     * Method will be called if the messages block in the components block is empty
     *
     * @param allContent     All content from YAML-file
     * @param messagesMap    map of content messages
     * @param excludeSchemas set of schemas, which willn't be generated
     */
    private void fillMessagesByChannel(Map<String, Object> allContent, Map<String, Object> messagesMap, Set<String> excludeSchemas) {
        Map<String, Object> channelsMap = castObjectToMap(allContent.get(CHANNELS));
        channelsMap.entrySet().forEach(
                entry -> {
                    String channelName = entry.getKey();

                    Map<String, Object> filteredSubscribeMap = castObjectToMap(entry.getValue()).entrySet().stream()
                            .filter(e -> e.getKey().equals(SUBSCRIBE))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    Map<String, Object> filteredPublishMap = castObjectToMap(entry.getValue()).entrySet().stream()
                            .filter(e -> e.getKey().equals(PUBLISH))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    if (!filteredSubscribeMap.isEmpty()) {
                        fillMessageFromChannel(allContent, messagesMap, excludeSchemas, filteredSubscribeMap, channelName, SUBSCRIBE);
                    }
                    if (!filteredPublishMap.isEmpty()) {
                        fillMessageFromChannel(allContent, messagesMap, excludeSchemas, filteredPublishMap, channelName, PUBLISH);
                    }

                    System.out.println("MESSAGES MAP AFTER MAPPING - " + messagesMap);
                }
        );
    }

    /**
     * Main method for preparing shemas
     *
     * @param processContext Context with all properties
     */
    private void processSchemas(ProcessContext processContext) {
        System.out.println(LOG_DELIMETER);
        List<Schema> schemaList =
                schemaMapper.mapSchemasToObjects(processContext);
        if (!schemaList.isEmpty()) {
            System.out.println();
            System.out.println("START WRITING JAVA CLASS FROM SCHEMAS:");
            schemaList.forEach(schema -> System.out.println(schema.getSchemaName()));
            System.out.println();
            writeSchemas(processContext.getPathToWrite(), processContext.getOutputDirectoryName(), schemaList);
            System.out.println(LOG_DELIMETER + ANSI_RESET);
            System.out.println();
        }
    }

    /**
     * Main method for preparing messages
     *
     * @param processContext Context with all properties
     */
    private void processMessages(ProcessContext processContext) {
        System.out.println(ANSI_CYAN + LOG_DELIMETER);
        List<Message> messageList =
                messageMapper.mapMessagesToObjects(processContext);
        System.out.println();
        System.out.println("START WRITING JAVA CLASS FROM MESSAGES:");
        messageList.forEach(message -> System.out.println(message.getMessageName()));
        System.out.println();
        writeMessages(processContext.getPathToWrite(), processContext.getOutputDirectoryName(), processContext.getPackageLocation(), messageList);
        System.out.println(LOG_DELIMETER);
        System.out.println();
    }

    /**
     * Preliminary analysis of the schemas
     *
     * @param filePath   path to file (used for logging only)
     * @param schemasMap map of schemas from components block
     */
    private void analyzeSchemas(String filePath, Map<String, Object> schemasMap) {
        List<String> incorrectFilledProperties = new ArrayList<>();
        System.out.println();
        System.out.println(ANSI_PURPLE + LOG_DELIMETER);
        System.out.println("Starting Analyze AsyncAPI: " + filePath);
        Map<String, Object> mapAllOf = new LinkedHashMap<>();
        schemasMap.forEach((schemaName, schemaValues) -> {
            System.out.println("ANALYZING OF SCHEMA: " + schemaName);
            System.out.println(schemaValues);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            String reference = getStringValueIfExistOrElseNull(REFERENCE, schemaMap);
            String allOf = getStringValueIfExistOrElseNull(ALL_OF, schemaMap);
            if (allOf != null) {
                mapAllOf.put(schemaName, schemaValues);
                System.out.println(ANSI_GREEN + schemaName + " Values: " + schemaValues + ANSI_PURPLE);
            }
            if (schemaType != null || allOf != null) {
                Map<String, Object> propertiesMap = castObjectToMap(schemaMap.get(PROPERTIES));
                if (!propertiesMap.isEmpty() && schemaType != null) {
                    propertiesMap.forEach((propName, propValues) -> {
                        Map<String, Object> propertyValueMap = castObjectToMap(propValues);
                        if (getStringValueIfExistOrElseNull(TYPE, propertyValueMap) == null &&
                                getStringValueIfExistOrElseNull(REFERENCE, propertyValueMap) == null) {
                            incorrectFilledProperties.add("Schema: " + schemaName + " TYPE not found: " + propName);
                        }
                    });
                } else {
                    if (schemaType == null) {
                        incorrectFilledProperties.add("NOT DEFINED TYPE OF SCHEMA! Schema: " + schemaName);
                    }
                }
            }
        });

        if (!incorrectFilledProperties.isEmpty()) {
            System.out.println();
            System.out.println(ANSI_RED + "ANALIZE FAILED! PLEASE, CHECK CONTRACT ATTRIBUTES FILLING!");
            throwException(incorrectFilledProperties);
        }
        System.out.println("ANALYZE FINISH SUCCESSFULLY!");
        System.out.println(LOG_DELIMETER + ANSI_RESET);
        System.out.println();
    }

    /**
     * Method checks and return connected yaml
     *
     * @param file    Yaml file
     * @param content Content from Yaml file
     * @return content
     */
    private static String getContent(String content, String packageLocation) {
        // Regular expression to find the line with $ref, including indentation
        String regex = "\\s*\\$ref:\\s*['\"](\\./[^'\"]*\\.yaml)(?:#/(.*))?['\"]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        StringBuilder modifiedContent = new StringBuilder();
        int lastEnd = 0;

        // Check if a match is found
        while (matcher.find()) {
            // Add the part of the string before the match
            modifiedContent.append(content, lastEnd, matcher.start());

            // Extract the class name from the fragment
            String className = matcher.group(2); // Get the fragment if it exists
            if (className != null) {
                className = className.substring(className.lastIndexOf('/') + 1);
            }
            String pathName = matcher.group(1).replace("./", "").replace(".yaml", ""); // If there is no fragment, use the file name


            // Determine the indentation from the original line
            String originalLine = matcher.group(0); // Includes indentation
            String indentation = originalLine.substring(0, originalLine.indexOf("$ref:")); // Get the indentation

            // Form the new line with the correct indentation
            String replaceName = String.format("%sname: %s", indentation, className);
            String replacePackage = String.format("%spackage: %s", indentation,
                    packageLocation.isBlank() ?
                            pathName :
                            packageLocation + "." + toLowerCaseFirstChar(pathName) + "." + "common").replaceAll("/", ".");

            // Add the new line to the result
            modifiedContent.append(replaceName).append("\n").append(replacePackage).append("\n");

            // Update the end of the last match
            lastEnd = matcher.end();
        }

        // Add the remaining part of the string after the last match
        modifiedContent.append(content.substring(lastEnd));

        return modifiedContent.toString();
    }

    public static String toLowerCaseFirstChar(String input) {
        if (input == null || input.isEmpty()) {
            return input; // return null or Empty String
        }
        // Convert the first character to lowercase and concatenate it with the rest of the string
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }


    /**
     * @param outputDirectory output directory for pojos
     * @param messageList     Mapped messages to pojo view
     */
    private void writeMessages(String outputDirectory, String outputDirectoryName, String packageLocation, List<Message> messageList) {
        messageList.forEach(message -> {
            String messagesPath;
            if (message.getPathForGenerateMessage() != null) {
                String preparePath = String.join(DELIMITER, outputDirectory,
                        message.getPathForGenerateMessage().replaceAll("\\.", DELIMITER));
                messagesPath = String.join(DELIMITER, preparePath) + DELIMITER;
                message.setMessagePackageName(String.join(DELIMITER, packageLocation, outputDirectoryName,
                        message.getPathForGenerateMessage()).replaceAll("/", ".").concat(";"));
            } else {
                //Create a file by the name of a specific message and write then
                messagesPath = String.join(DELIMITER, outputDirectory, "messages") + DELIMITER;
            }
            writeFile(messagesPath, message.getMessageName(), message.toWrite());
        });
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param schemaList      Mapped schemas to pojo view
     */
    private void writeSchemas(String outputDirectory, String outputDirectoryName, List<Schema> schemaList) {
        schemaList.forEach(schema -> {
            String schemasPath = String.join(DELIMITER, outputDirectory, "common") + DELIMITER;
            //Create a file by the name of a specific schema and write then
            writeFile(schemasPath, schema.getSchemaName(), schema.toWrite());
        });
    }

    private static void writeFile(String messagesPath, String fileName, String fileToWrite) {
        new File(messagesPath).mkdirs();
        File file = new File(messagesPath + fileName + ".java");
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            pw.write(fileToWrite);
            pw.flush();
            System.out.println("The file " + fileName + " has been written");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method unused. Maybe in the next features will be used.
     * The method checks the equality of filling schemes.
     * It was originally conceived for polymorphism.
     *
     * @param checkContent content for check
     * @return result of equals
     */
    private boolean equalsPropertiesContent(List<Map<String, Object>> checkContent) {
        boolean isEqualsContent = false;
        for (int i = 0; checkContent.size() > i; i++) {
            if (i != 0 && isEqualsContent == false) {
                break;
            }
            for (int j = 1; checkContent.size() > j; j++) {
                boolean equals = checkContent.get(i).values().toString().equals(checkContent.get(j).values().toString());
                if (equals) {
                    isEqualsContent = equals;
                } else {
                    isEqualsContent = equals;
                    break;
                }
            }
        }

        System.out.println("CONTENT IS EQUALS: " + isEqualsContent);
        return isEqualsContent;
    }

    /**
     * @param messages list of message names
     */
    private void throwException(List<String> messages) {
        throw new SchemaFillException(messages.toString());
    }
}