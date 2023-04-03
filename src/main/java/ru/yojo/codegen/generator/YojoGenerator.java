package ru.yojo.codegen.generator;

import org.yaml.snakeyaml.Yaml;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.Message;
import ru.yojo.codegen.domain.Schema;
import ru.yojo.codegen.mapper.MessageMapper;
import ru.yojo.codegen.mapper.SchemaMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getPackage;

/**
 * POJO Generator
 * <p>
 * To use generator in your project you need just inject bean Generator
 * <p>
 * Developed by Vladimir Morozkin
 * March 2023
 */
@SuppressWarnings("all")
public class YojoGenerator implements Generator {

    private final SchemaMapper schemaMapper;
    private final MessageMapper messageMapper;

    public YojoGenerator(SchemaMapper schemaMapper, MessageMapper messageMapper) {
        this.schemaMapper = schemaMapper;
        this.messageMapper = messageMapper;
    }

    /**
     * Main method for generate POJO
     */
    @Override
    public void generate(String filePath, String outputDirectory, String packageLocation, LombokProperties lombokProperties) {

        Map<String, Object> obj;
        try {
            obj = new Yaml().load(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
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

        Map<String, Object> schemasMap = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("schemas"));
        List<Schema> schemaList =
                schemaMapper.mapSchemasToObjects(
                        castObjectToMap(schemasMap),
                        lombokProperties,
                        commonPackage);
        writeSchemas(output, outputDirectoryName, schemaList);

        Map<String, Object> messages = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("messages"));
        List<Message> messageList =
                messageMapper.mapMessagesToObjects(
                        messages,
                        lombokProperties,
                        messagePackage,
                        commonPackage);
        writeMessages(output, outputDirectoryName, messageList);
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param messageList     Mapped messages to pojo view
     */
    private void writeMessages(String outputDirectory, String outputDirectoryName, List<Message> messageList) {
        messageList.forEach(message -> {
            //Create a file by the name of a specific message and write then
            String messagesPath = String.join(DELIMETER.getValue(), outputDirectory, "messages") + DELIMETER.getValue();
            writeFile(messagesPath, message.getMessageName(), message.toString());
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
            writeFile(schemasPath, schema.getSchemaName(), schema.toString());
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