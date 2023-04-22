package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.MessageImplementationProperties;
import ru.yojo.codegen.domain.message.Message;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.yojo.codegen.constants.ConstantsEnum.*;
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
    public void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties, MessageImplementationProperties messageImplementationProperties) {
        Map<String, Object> obj;
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            obj = new Yaml().load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String outputDirectoryName = new File(filePath).getName().replaceAll("\\..*", "");
        if (!outputDirectory.endsWith("/")) {
            outputDirectory = outputDirectory + DELIMETER.getValue();
        }
        String output = outputDirectory + outputDirectoryName;
        new File(output).mkdirs();

        String messagePackage = getPackage(packageLocation, outputDirectoryName, MESSAGE_PACKAGE_IMPORT);
        String commonPackage = getPackage(packageLocation, outputDirectoryName, COMMON_PACKAGE_IMPORT);

        Map<String, Object> messagesMap = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("messages"));
        Map<String, Object> schemasMap = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("schemas"));

        analyzeSchemas(filePath, schemasMap);

        processMessages(lombokProperties, messageImplementationProperties, outputDirectoryName, output, messagePackage, commonPackage, messagesMap, schemasMap);
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

    private void processMessages(LombokProperties lombokProperties, MessageImplementationProperties messageImplementationProperties, String outputDirectoryName, String output, String messagePackage, String commonPackage, Map<String, Object> messagesMap, Map<String, Object> schemasMap) {
        System.out.println(ANSI_CYAN + LOG_DELIMETER);
        List<Message> messageList =
                messageMapper.mapMessagesToObjects(
                        messagesMap,
                        schemasMap,
                        lombokProperties,
                        messagePackage,
                        commonPackage,
                        messageImplementationProperties);
        System.out.println();
        System.out.println("START WRITING JAVA CLASS FROM MESSAGES:");
        messageList.forEach(message -> System.out.println(message.getMessageName()));
        System.out.println();
        writeMessages(output, outputDirectoryName, messageList);
        System.out.println(LOG_DELIMETER);
        System.out.println();
    }

    private static void analyzeSchemas(String filePath, Map<String, Object> schemasMap) {
        Set<String> incorrectFilledProperties = new HashSet<>();
        System.out.println();
        System.out.println(ANSI_PURPLE + LOG_DELIMETER);
        System.out.println("Starting Analyze AsyncAPI: " + filePath);
        schemasMap.forEach((schemaName, schemaValues) -> {
            System.out.println("ANALYZING OF SCHEMA: " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            if (schemaType != null) {
                Map<String, Object> propertiesMap = castObjectToMap(schemaMap.get(PROPERTIES.getValue()));
                propertiesMap.forEach((propName, propValues) -> {
                    Map<String, Object> propertyValueMap = castObjectToMap(propValues);
                    if (getStringValueIfExistOrElseNull(TYPE, propertyValueMap) == null &&
                            getStringValueIfExistOrElseNull(REFERENCE, propertyValueMap) == null) {
                        incorrectFilledProperties.add("Schema: " + schemaName + " TYPE not found: " + propName);
                    }
                });
            } else {
                throw new SchemaFillException("NOT DEFINED TYPE OF SCHEMA! Schema: " + schemaName);
            }
        });

        if (!incorrectFilledProperties.isEmpty()) {
            incorrectFilledProperties.stream().sorted().forEach(System.err::println);
            throw new SchemaFillException(incorrectFilledProperties.toString());
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
            String messagesPath = String.join(DELIMETER.getValue(), outputDirectory, "messages") + DELIMETER.getValue();
            writeFile(messagesPath, message.getMessageName(), message.toWrite());
        });
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param schemaList      Mapped schemas to pojo view
     */
    private void writeSchemas(String outputDirectory, String outputDirectoryName, List<Schema> schemaList) {
        schemaList.forEach(schema -> {
            String schemasPath = String.join(DELIMETER.getValue(), outputDirectory, "common") + DELIMETER.getValue();
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
}