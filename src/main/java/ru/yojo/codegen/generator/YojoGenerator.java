package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LogUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

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
     * Method for generating from all files in directory
     *
     * @param directory        - which directory use for search contracts
     * @param outputDirectory  - directory to output generated classes
     * @param packageLocation  - package, which will write in each generated class
     * @param lombokProperties - properies of lombok
     * @throws IOException
     */
    @Override
    public void generateAll(String directory, String outputDirectory, String packageLocation, LombokProperties lombokProperties) throws IOException {
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
                            content = getContent(file, content);
                            if (content.startsWith("asyncapi")) {
                                Map<String, Object> obj = new Yaml().load(content);
                                process(file.getPath(), outputDirectory, packageLocation, lombokProperties, obj);
                            }
                        }
                );
    }

    /**
     * Main method for generate POJO
     */
    @Override
    public void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties) {
        Map<String, Object> obj;
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            obj = new Yaml().load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        process(filePath, outputDirectory, packageLocation, lombokProperties, obj);
    }

    private void process(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties, Map<String, Object> obj) {
        String outputDirectoryName = new File(filePath).getName().replaceAll("\\..*", "");
        if (!outputDirectory.endsWith("/")) {
            outputDirectory = outputDirectory + DELIMITER;
        }
        String output = outputDirectory + outputDirectoryName;
        new File(output).mkdirs();

        String messagePackage = getPackage(packageLocation, outputDirectoryName, MESSAGE_PACKAGE_IMPORT);
        String commonPackage = getPackage(packageLocation, outputDirectoryName, COMMON_PACKAGE_IMPORT);

        Map<String, Object> messagesMap =
                castObjectToMap(
                        castObjectToMap(
                                castObjectToMap(obj.get("components"))).get("messages"));
        Map<String, Object> schemasMap =
                castObjectToMap(
                        castObjectToMap(
                                castObjectToMap(obj.get("components"))).get("schemas"));

        analyzeSchemas(filePath, schemasMap);

        processMessages(lombokProperties, outputDirectoryName, output, messagePackage, commonPackage, messagesMap, schemasMap);
        processSchemas(lombokProperties, outputDirectoryName, output, commonPackage, schemasMap);

        System.out.println(LOG_FINISH);
    }

    private void processSchemas(LombokProperties lombokProperties, String outputDirectoryName, String output, String commonPackage, Map<String, Object> schemasMap) {
        System.out.println(LOG_DELIMETER);
        List<Schema> schemaList =
                schemaMapper.mapSchemasToObjects(
                        schemasMap,
                        lombokProperties,
                        commonPackage);
        if (!schemaList.isEmpty()) {
            System.out.println();
            System.out.println("START WRITING JAVA CLASS FROM SCHEMAS:");
            schemaList.forEach(schema -> System.out.println(schema.getSchemaName()));
            System.out.println();
            writeSchemas(output, outputDirectoryName, schemaList);
            System.out.println(LOG_DELIMETER + ANSI_RESET);
            System.out.println();
        }
    }

    private void processMessages(LombokProperties lombokProperties, String outputDirectoryName, String output, String messagePackage, String commonPackage, Map<String, Object> messagesMap, Map<String, Object> schemasMap) {
        System.out.println(ANSI_CYAN + LOG_DELIMETER);
        List<Message> messageList =
                messageMapper.mapMessagesToObjects(
                        messagesMap,
                        schemasMap,
                        lombokProperties,
                        messagePackage,
                        commonPackage);
        System.out.println();
        System.out.println("START WRITING JAVA CLASS FROM MESSAGES:");
        messageList.forEach(message -> System.out.println(message.getMessageName()));
        System.out.println();
        writeMessages(output, outputDirectoryName, messageList);
        System.out.println(LOG_DELIMETER);
        System.out.println();
    }

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
    private static String getContent(File file, String content) {
        if (content.contains("./")) {
            System.out.println("FOUND SEPARATED CONTRACT! " + file.getName());
            List<String> contentByLines = content.lines().collect(Collectors.toList());

            AtomicInteger lineNumber = new AtomicInteger();
            content.lines()
                    //key - number of line, value - line with content
                    .collect(Collectors.toMap(line -> lineNumber.incrementAndGet(), Function.identity()))
                    .entrySet().stream()
                    //try to find reference to other contract
                    .filter(entry -> entry.getValue().contains("./"))
                    .forEach(entry -> {
                        String contentFromSeparatedFile;
                        try {
                            String line = entry.getValue();
                            contentFromSeparatedFile = new String(Files.readAllBytes(Paths.get(
                                    new File(file.getParent() + line.substring(line.indexOf(".") + 1, line.indexOf("#"))).getPath())));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        contentByLines.remove(entry.getKey() - 1);
                        contentByLines.remove(entry.getKey() - 2);
                        contentByLines.addAll(entry.getKey() - 2, contentFromSeparatedFile.lines()
                                .map(l -> TABULATION + l)
                                .collect(Collectors.toList()));
                    });
            content = String.join(lineSeparator(), contentByLines);
        }
        return content;
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param messageList     Mapped messages to pojo view
     */
    private void writeMessages(String outputDirectory, String outputDirectoryName, List<Message> messageList) {
        messageList.forEach(message -> {
            //Create a file by the name of a specific message and write then
            String messagesPath = String.join(DELIMITER, outputDirectory, "messages") + DELIMITER;
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

    private void throwException(List<String> messages) {
        throw new SchemaFillException(messages.toString());
    }
}