package ru.yojo.codegen.generator;

import org.springframework.stereotype.Component;
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

import static ru.yojo.codegen.constants.ConstantsEnum.DELIMETER;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;

/**
 * POJO Generator
 * <p>
 * To use generator in your project you need just inject bean Generator
 * <p>
 * Developed by Vladimir Morozkin
 * March 2023
 */
@Component
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
     * Schema-based generation:
     * components:
     * messages:
     * schemas:
     * SchemaToPojo
     */
    @Override
    public void generate(String filePath, String outputDirectory, LombokProperties lombokProperties) {
        Map<String, Object> obj;
        try {
            obj = new Yaml().load(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String outputDirectoryName = new File(filePath).getName().replaceAll("\\..*", "");
        String output = outputDirectory + DELIMETER.getValue() + outputDirectoryName;
        new File(output).mkdirs();

        Map<String, Object> messages = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("messages"));
        List<Message> messageList = messageMapper.mapMessagesToObjects(messages, lombokProperties);
        writeMessages(output, messageList);

        Map<String, Object> schemasMap = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("schemas"));
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(castObjectToMap(schemasMap), lombokProperties);
        writeSchemas(output, schemaList);
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param messageList     Mapped messages to pojo view
     */
    private void writeMessages(String outputDirectory, List<Message> messageList) {
        messageList.forEach(message -> {
            //Create a file by the name of a specific message and write then
            String messagesPath = outputDirectory + DELIMETER.getValue() + "messages" + DELIMETER.getValue();
            new File(messagesPath).mkdirs();
            File file = new File(messagesPath + message.getMessageName() + ".java");
            try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
                pw.write(message.toString());
                pw.flush();
                System.out.println("The file has been written");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param schemaList      Mapped schemas to pojo view
     */
    private void writeSchemas(String outputDirectory, List<Schema> schemaList) {
        schemaList.forEach(schema -> {
            String schemasPath = outputDirectory + DELIMETER.getValue() + "common" + DELIMETER.getValue();
            //Create a file by the name of a specific schema and write then
            new File(schemasPath).mkdirs();
            File file = new File(schemasPath + schema.getSchemaName() + ".java");
            try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
                pw.write(schema.toString());
                pw.flush();
                System.out.println("The file has been written");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}