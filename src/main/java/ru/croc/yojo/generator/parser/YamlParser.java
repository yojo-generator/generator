package ru.croc.yojo.generator.parser;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.LoaderOptions;
import ru.croc.yojo.generator.record.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.croc.yojo.generator.util.YamlUtils.*;

/**
 * Парсер YAML спецификаций AsyncAPI.
 * Использует современные возможности Java для безопасной обработки данных.
 */
public class YamlParser {

    private final Yaml yaml;

    public YamlParser() {
        LoaderOptions loaderOptions = new LoaderOptions();
        this.yaml = new Yaml(new SafeConstructor(loaderOptions));
    }

    /**
     * Парсит AsyncAPI спецификацию из потока.
     *
     * @param yamlStream поток с YAML данными
     * @return AsyncApiSpec объект
     */
    public AsyncApiSpec parseAsyncApiSpec(InputStream yamlStream) {
        Map<String, Object> rawSpec = yaml.load(yamlStream);
        
        String asyncapi = getStringValue(rawSpec, ASYNCAPI);
        String id = getStringValue(rawSpec, ID);
        String title = getStringValue(rawSpec, TITLE);
        String version = getStringValue(rawSpec, VERSION);
        
        List<ComponentSchema> components = parseComponents(getMapValue(rawSpec, COMPONENTS));
        
        return new AsyncApiSpec(asyncapi, id, title, version, components, rawSpec);
    }

    /**
     * Парсит компоненты схемы из YAML.
     *
     * @param componentsMap карта компонентов
     * @return список компонентов схемы
     */
    private List<ComponentSchema> parseComponents(Map<String, Object> componentsMap) {
        if (componentsMap == null) {
            return List.of();
        }

        List<ComponentSchema> components = new ArrayList<>();
        
        Map<String, Object> schemas = getMapValue(componentsMap, SCHEMAS);
        if (schemas != null) {
            for (Map.Entry<String, Object> entry : schemas.entrySet()) {
                String name = entry.getKey();
                Object schemaObj = entry.getValue();
                
                if (schemaObj instanceof Map) {
                    Map<String, Object> schemaMap = (Map<String, Object>) schemaObj;
                    YamlSchema schema = parseSchema(schemaMap);
                    components.add(new ComponentSchema(name, schema, schemaMap));
                }
            }
        }
        
        return components;
    }

    /**
     * Парсит схему из YAML.
     *
     * @param schemaMap карта схемы
     * @return YamlSchema объект
     */
    private YamlSchema parseSchema(Map<String, Object> schemaMap) {
        String title = getStringValue(schemaMap, TITLE);
        String description = getStringValue(schemaMap, DESCRIPTION);
        List<YamlNode> properties = parseProperties(getMapValue(schemaMap, PROPERTIES));
        
        return new YamlSchema(title, description, properties, schemaMap);
    }

    /**
     * Парсит свойства из YAML.
     *
     * @param propertiesMap карта свойств
     * @return список узлов YAML
     */
    private List<YamlNode> parseProperties(Map<String, Object> propertiesMap) {
        if (propertiesMap == null) {
            return List.of();
        }

        List<YamlNode> properties = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            String name = entry.getKey();
            Object propertyObj = entry.getValue();
            
            if (propertyObj instanceof Map) {
                Map<String, Object> propertyMap = (Map<String, Object>) propertyObj;
                String type = getStringValue(propertyMap, TYPE);
                String description = getStringValue(propertyMap, DESCRIPTION);
                
                // Рекурсивно парсим вложенные свойства для объектов
                List<YamlNode> nestedProperties = List.of();
                if ("object".equals(type)) {
                    nestedProperties = parseProperties(getMapValue(propertyMap, PROPERTIES));
                }
                
                properties.add(new YamlNode(name, type, description, nestedProperties, propertyMap));
            }
        }
        
        return properties;
    }
}