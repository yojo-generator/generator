package ru.yojo.yamltopojo.generator;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.yojo.yamltopojo.domain.LombokProperties;
import ru.yojo.yamltopojo.domain.Schema;
import ru.yojo.yamltopojo.mapper.SchemaMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static ru.yojo.yamltopojo.util.MapperUtil.castObjectToMap;

/**
 * POJO Generator
 * <p>
 * To use generator in your project you need just inject bean Generator
 * <p>
 * Developed by Vladimir Morozkin
 * March 2023
 */
@Component
public class YojoGenerator implements Generator {

    private final SchemaMapper schemaMapper;

    public YojoGenerator(SchemaMapper schemaMapper) {
        this.schemaMapper = schemaMapper;
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
        Map<String, Object> schemasMap = castObjectToMap(castObjectToMap(castObjectToMap(obj.get("components"))).get("schemas"));
        List<Schema> schemaList = schemaMapper.mapSchemasToObjects(castObjectToMap(schemasMap), lombokProperties);
        write(outputDirectory, schemaList);
    }

    /**
     * @param outputDirectory output directory for pojos
     * @param schemaList      Mapped schemas to pojo view
     */
    private void write(String outputDirectory, List<Schema> schemaList) {
        schemaList.forEach(schema -> {
            //Create a file by the name of a specific schema and write then
            File file = new File(outputDirectory + "/" + schema.getSchemaName() + ".java");
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