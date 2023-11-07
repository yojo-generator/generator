package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
     * Main method for generate POJO
     */
    @Override
    public void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties) {
        Map<String, Object> obj;
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            obj = new Yaml().load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        Map<String, Object> mapAllOf = new HashMap<>();
        schemasMap.forEach((schemaName, schemaValues) -> {
            System.out.println("ANALYZING OF SCHEMA: " + schemaName);
            System.out.println(schemaValues);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
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
                    incorrectFilledProperties.add("NOT DEFINED TYPE OF SCHEMA! Schema: " + schemaName);
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