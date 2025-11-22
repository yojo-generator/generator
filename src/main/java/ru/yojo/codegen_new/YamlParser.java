package ru.yojo.codegen_new;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yojo.codegen_new.TypeMapper.mapToJavaType;

/**
 * Парсер YAML контрактов AsyncAPI
 */
public class YamlParser {

    private final Yaml yaml = new Yaml(new SafeConstructor());

    /**
     * Парсит YAML контракт и возвращает AsyncApiSpec
     */
    public AsyncApiSpec parseAsyncApiSpec(String yamlContent) {
        Map<String, Object> content = yaml.load(yamlContent);
        return parseAsyncApiSpec(content);
    }

    /**
     * Парсит YAML контракт и возвращает AsyncApiSpec
     */
    public AsyncApiSpec parseAsyncApiSpec(InputStream inputStream) {
        Map<String, Object> content = yaml.load(inputStream);
        return parseAsyncApiSpec(content);
    }

    private AsyncApiSpec parseAsyncApiSpec(Map<String, Object> content) {
        String asyncApiVersion = (String) content.get("asyncapi");
        Map<String, Object> info = (Map<String, Object>) content.get("info");
        String title = info != null ? (String) info.get("title") : null;
        String version = info != null ? (String) info.get("version") : null;

        Map<String, Object> components = (Map<String, Object>) content.get("components");
        Map<String, Object> schemas = components != null ? (Map<String, Object>) components.get("schemas") : new HashMap<>();
        Map<String, Object> messages = components != null ? (Map<String, Object>) components.get("messages") : new HashMap<>();

        Map<String, Schema> parsedSchemas = parseSchemas(schemas);
        Map<String, Message> parsedMessages = parseMessages(messages, schemas);

        return new AsyncApiSpec(asyncApiVersion, title, version, parsedSchemas, parsedMessages);
    }

    private Map<String, Schema> parseSchemas(Map<String, Object> schemas) {
        Map<String, Schema> parsedSchemas = new HashMap<>();
        if (schemas != null) {
            for (Map.Entry<String, Object> entry : schemas.entrySet()) {
                String schemaName = entry.getKey();
                Map<String, Object> schemaContent = (Map<String, Object>) entry.getValue();
                Schema schema = parseSchema(schemaName, schemaContent, schemas);
                parsedSchemas.put(schemaName, schema);
            }
        }
        return parsedSchemas;
    }

    private Schema parseSchema(String name, Map<String, Object> content, Map<String, Object> allSchemas) {
        YamlNode yamlNode = parseYamlNode(content);
        
        // Если это enum
        if (yamlNode.enumeration() != null && !yamlNode.enumeration().isEmpty()) {
            Map<String, String> enumValues = createEnumValues(yamlNode.enumeration(), (Map<String, Object>) content.get("x-enumNames"));
            return new Schema(
                capitalize(name),
                "ru.yojo.generated", // будет заменено на реальный пакет
                yamlNode.description(),
                yamlNode,
                List.of(), // для enum полей нет
                Set.of("com.fasterxml.jackson.annotation.JsonProperty", "com.fasterxml.jackson.annotation.JsonCreator"),
                Set.of(),
                null,
                true,
                enumValues,
                false,
                Map.of()
            );
        }

        // Если это полиморфизм (oneOf, allOf, anyOf)
        if (!yamlNode.oneOf().isEmpty() || !yamlNode.allOf().isEmpty() || !yamlNode.anyOf().isEmpty()) {
            return parsePolymorphicSchema(name, yamlNode, allSchemas);
        }

        // Обычная схема с полями
        List<Field> fields = parseFields(yamlNode.properties(), allSchemas);
        Set<String> imports = collectImports(fields);
        
        // Добавляем импорты для коллекций
        if (fields.stream().anyMatch(Field::isArray)) {
            imports = new HashSet<>(imports);
            imports.add("java.util.List");
            imports.add("java.util.ArrayList");
        }
        if (fields.stream().anyMatch(Field::isMap)) {
            imports = new HashSet<>(imports);
            imports.add("java.util.Map");
            imports.add("java.util.HashMap");
        }

        // Обработка extends и implements
        String superClass = null;
        Set<String> interfaces = new HashSet<>();
        if (content.containsKey("extends")) {
            Map<String, Object> extendsMap = (Map<String, Object>) content.get("extends");
            superClass = (String) extendsMap.get("fromClass");
        }
        if (content.containsKey("implements")) {
            Map<String, Object> implementsMap = (Map<String, Object>) content.get("implements");
            List<String> interfaceList = (List<String>) implementsMap.get("fromInterface");
            if (interfaceList != null) {
                interfaces.addAll(interfaceList.stream()
                    .map(iface -> iface.substring(iface.lastIndexOf('.') + 1))
                    .collect(Collectors.toList()));
            }
        }

        return new Schema(
            capitalize(name),
            "ru.yojo.generated",
            yamlNode.description(),
            yamlNode,
            fields,
            imports,
            interfaces,
            superClass,
            false,
            Map.of(),
            false,
            Map.of()
        );
    }

    private Schema parsePolymorphicSchema(String name, YamlNode yamlNode, Map<String, Object> allSchemas) {
        // Для полиморфных схем создаем абстрактный класс или интерфейс
        Set<String> imports = new HashSet<>();
        imports.add("com.fasterxml.jackson.annotation.JsonSubTypes");
        imports.add("com.fasterxml.jackson.annotation.JsonTypeInfo");

        // Объединяем поля из всех подтипов
        List<Field> allFields = new ArrayList<>();
        Set<String> subTypes = new HashSet<>();

        // Обработка oneOf
        for (Object oneOfItem : yamlNode.oneOf()) {
            if (oneOfItem instanceof Map) {
                Map<String, Object> itemMap = (Map<String, Object>) oneOfItem;
                String ref = (String) itemMap.get("$ref");
                if (ref != null) {
                    String refName = ref.substring(ref.lastIndexOf('/') + 1);
                    subTypes.add(refName);
                    Schema subSchema = parseSchema(refName, (Map<String, Object>) allSchemas.get(refName), allSchemas);
                    allFields.addAll(subSchema.fields());
                }
            }
        }

        return new Schema(
            capitalize(name),
            "ru.yojo.generated",
            yamlNode.description(),
            yamlNode,
            allFields.stream().distinct().collect(Collectors.toList()), // уникальные поля
            imports,
            Set.of(),
            null,
            false,
            Map.of(),
            false,
            Map.of()
        );
    }

    private List<Field> parseFields(Map<String, Object> properties, Map<String, Object> allSchemas) {
        List<Field> fields = new ArrayList<>();
        if (properties != null) {
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String fieldName = entry.getKey();
                Map<String, Object> fieldContent = (Map<String, Object>) entry.getValue();
                
                Field field = parseField(fieldName, fieldContent, allSchemas);
                fields.add(field);
            }
        }
        return fields;
    }

    private Field parseField(String name, Map<String, Object> content, Map<String, Object> allSchemas) {
        String type = (String) content.get("type");
        String format = (String) content.get("format");
        String ref = (String) content.get("$ref");
        String description = (String) content.get("description");
        boolean required = false; // будет установлено позже из required списка

        String javaType;
        Set<String> imports = new HashSet<>();
        boolean isArray = false;
        boolean isMap = false;
        String keyType = null;
        String valueType = null;

        if (ref != null) {
            // Обработка ссылки на другую схему
            String refName = ref.substring(ref.lastIndexOf('/') + 1);
            javaType = capitalize(refName);
            imports.add("ru.yojo.generated." + javaType);
        } else if ("array".equals(type)) {
            // Обработка массива
            isArray = true;
            Map<String, Object> items = (Map<String, Object>) content.get("items");
            if (items != null) {
                String itemType = (String) items.get("type");
                String itemRef = (String) items.get("$ref");
                
                if (itemRef != null) {
                    String refName = itemRef.substring(itemRef.lastIndexOf('/') + 1);
                    valueType = capitalize(refName);
                    imports.add("ru.yojo.generated." + valueType);
                } else if ("object".equals(itemType)) {
                    // Если элемент массива - объект, и есть $ref внутри
                    String itemRefInside = (String) items.get("$ref");
                    if (itemRefInside != null) {
                        String refName = itemRefInside.substring(itemRefInside.lastIndexOf('/') + 1);
                        valueType = capitalize(refName);
                        imports.add("ru.yojo.generated." + valueType);
                    } else {
                        valueType = "Object";
                    }
                } else {
                    valueType = mapToJavaType(itemType, format);
                    if ("UUID".equals(valueType)) {
                        imports.add("java.util.UUID");
                    }
                }
            } else {
                valueType = "Object";
            }
            javaType = "List<" + valueType + ">";
        } else if ("object".equals(type) && content.containsKey("additionalProperties")) {
            // Обработка мапы
            isMap = true;
            Map<String, Object> additionalProps = (Map<String, Object>) content.get("additionalProperties");
            
            if (format != null && "uuid".equals(format)) {
                keyType = "UUID";
                imports.add("java.util.UUID");
            } else {
                keyType = "String";
            }
            
            String propType = (String) additionalProps.get("type");
            String propRef = (String) additionalProps.get("$ref");
            
            if (propRef != null) {
                String refName = propRef.substring(propRef.lastIndexOf('/') + 1);
                valueType = capitalize(refName);
                imports.add("ru.yojo.generated." + valueType);
            } else if ("object".equals(propType)) {
                String propRefInside = (String) additionalProps.get("$ref");
                if (propRefInside != null) {
                    String refName = propRefInside.substring(propRefInside.lastIndexOf('/') + 1);
                    valueType = capitalize(refName);
                    imports.add("ru.yojo.generated." + valueType);
                } else {
                    valueType = "Object";
                }
            } else {
                valueType = mapToJavaType(propType, format);
                if ("UUID".equals(valueType)) {
                    imports.add("java.util.UUID");
                }
            }
            javaType = "Map<" + keyType + ", " + valueType + ">";
        } else {
            // Обычный скалярный тип
            javaType = mapToJavaType(type, format);
            if ("UUID".equals(javaType)) {
                imports.add("java.util.UUID");
            }
        }

        // Проверяем, является ли это enum
        boolean isEnum = content.containsKey("enum");
        if (isEnum) {
            javaType = capitalize(name);
            imports.add("ru.yojo.generated." + javaType);
        }

        return new Field(
            name,
            type,
            javaType,
            description,
            required,
            isEnum,
            isArray,
            isMap,
            keyType,
            valueType,
            imports,
            null
        );
    }

    private Map<String, String> createEnumValues(List<String> enumList, Map<String, Object> enumNames) {
        Map<String, String> result = new HashMap<>();
        for (String enumValue : enumList) {
            String name = enumNames != null ? (String) enumNames.get(enumValue) : null;
            result.put(enumValue, name != null ? name : enumValue);
        }
        return result;
    }

    private Set<String> collectImports(List<Field> fields) {
        Set<String> imports = new HashSet<>();
        for (Field field : fields) {
            imports.addAll(field.imports());
            if (field.isArray()) {
                imports.add("java.util.List");
            }
            if (field.isMap()) {
                imports.add("java.util.Map");
            }
        }
        return imports;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private YamlNode parseYamlNode(Map<String, Object> content) {
        if (content == null) {
            return YamlNode.empty();
        }

        String type = (String) content.get("type");
        String format = (String) content.get("format");
        String reference = (String) content.get("$ref");
        String description = (String) content.get("description");
        List<String> enumeration = (List<String>) content.get("enum");
        Map<String, Object> properties = (Map<String, Object>) content.get("properties");
        Map<String, Object> additionalProperties = (Map<String, Object>) content.get("additionalProperties");
        List<Object> items = (List<Object>) content.get("items");
        List<Object> allOf = (List<Object>) content.get("allOf");
        List<Object> oneOf = (List<Object>) content.get("oneOf");
        List<Object> anyOf = (List<Object>) content.get("anyOf");
        String realization = (String) content.get("realization");
        String name = (String) content.get("name");
        String packageName = (String) content.get("package");
        Map<String, Object> enumNames = (Map<String, Object>) content.get("x-enumNames");

        return new YamlNode(
            type,
            format,
            reference,
            properties,
            items,
            additionalProperties,
            allOf,
            oneOf,
            anyOf,
            description,
            enumeration,
            enumNames != null ? 
                enumNames.entrySet().stream()
                    .collect(Collectors.toMap(
                        entry -> (String) entry.getKey(),
                        entry -> (String) entry.getValue()
                    )) : null,
            null, // extensions
            realization,
            null, // required
            name,
            packageName
        );
    }

    private Map<String, Message> parseMessages(Map<String, Object> messages, Map<String, Object> schemas) {
        Map<String, Message> parsedMessages = new HashMap<>();
        if (messages != null) {
            for (Map.Entry<String, Object> entry : messages.entrySet()) {
                String messageName = entry.getKey();
                Map<String, Object> messageContent = (Map<String, Object>) entry.getValue();
                Message message = parseMessage(messageName, messageContent, schemas);
                parsedMessages.put(messageName, message);
            }
        }
        return parsedMessages;
    }

    private Message parseMessage(String name, Map<String, Object> content, Map<String, Object> schemas) {
        Map<String, Object> payload = (Map<String, Object>) content.get("payload");
        String summary = (String) content.get("summary");
        
        if (payload != null) {
            // Если в payload есть $ref, то используем связанную схему
            String ref = (String) payload.get("$ref");
            if (ref != null) {
                String refName = ref.substring(ref.lastIndexOf('/') + 1);
                Schema schema = parseSchema(refName, (Map<String, Object>) schemas.get(refName), schemas);
                return new Message(capitalize(name), schema.packageName(), summary, schema.fields(), schema.imports());
            } else {
                // Иначе обрабатываем payload как вложенный объект
                YamlNode yamlNode = parseYamlNode(payload);
                List<Field> fields = parseFields(yamlNode.properties(), schemas);
                Set<String> imports = collectImports(fields);
                return new Message(capitalize(name), "ru.yojo.generated", summary, fields, imports);
            }
        }
        
        return new Message(capitalize(name), "ru.yojo.generated", summary, List.of(), Set.of());
    }
}