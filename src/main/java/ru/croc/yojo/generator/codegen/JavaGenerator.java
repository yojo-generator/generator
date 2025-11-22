package ru.croc.yojo.generator.codegen;

import ru.croc.yojo.generator.record.AsyncApiSpec;
import ru.croc.yojo.generator.record.ComponentSchema;
import ru.croc.yojo.generator.record.YamlNode;
import ru.croc.yojo.generator.record.YamlSchema;

import java.util.*;

import static ru.croc.yojo.generator.util.YamlUtils.*;

/**
 * Генератор Java классов из AsyncAPI спецификации.
 * Использует современные возможности Java для безопасной генерации кода.
 */
public class JavaGenerator {

    private final String packageName;
    private final Set<String> generatedClasses;

    public JavaGenerator(String packageName) {
        this.packageName = packageName;
        this.generatedClasses = new HashSet<>();
    }

    /**
     * Генерирует Java классы из AsyncAPI спецификации.
     *
     * @param spec спецификация AsyncAPI
     * @return список сгенерированных Java классов
     */
    public List<GeneratedClass> generate(AsyncApiSpec spec) {
        List<GeneratedClass> generatedClasses = new ArrayList<>();
        
        for (ComponentSchema component : spec.components()) {
            generatedClasses.addAll(generateClass(component, spec));
        }
        
        return generatedClasses;
    }

    /**
     * Генерирует Java класс из компонента схемы.
     *
     * @param component компонент схемы
     * @param spec      спецификация AsyncAPI
     * @return список сгенерированных Java классов
     */
    private List<GeneratedClass> generateClass(ComponentSchema component, AsyncApiSpec spec) {
        List<GeneratedClass> generatedClasses = new ArrayList<>();
        
        // Проверяем, не был ли класс уже сгенерирован
        if (this.generatedClasses.contains(component.name())) {
            return generatedClasses;
        }
        
        this.generatedClasses.add(component.name());
        
        // Генерируем основной класс
        String className = capitalizeFirstLetter(component.name());
        StringBuilder classBuilder = new StringBuilder();
        
        // Добавляем package
        classBuilder.append("package ").append(packageName).append(";\n\n");
        
        // Добавляем imports
        Set<String> imports = collectImports(component.schema());
        for (String importStr : imports) {
            classBuilder.append("import ").append(importStr).append(";\n");
        }
        if (!imports.isEmpty()) {
            classBuilder.append("\n");
        }
        
        // Добавляем Javadoc
        if (component.schema().description() != null) {
            classBuilder.append("/**\n");
            classBuilder.append(" * ").append(escapeJavadoc(component.schema().description())).append("\n");
            classBuilder.append(" */\n");
        }
        
        // Начинаем объявление класса
        classBuilder.append("public class ").append(className).append(" {\n\n");
        
        // Добавляем поля
        for (YamlNode property : component.schema().properties()) {
            String fieldName = property.name();
            String fieldType = mapType(property.type(), property.rawNode());
            
            // Добавляем Javadoc для поля
            if (property.description() != null) {
                classBuilder.append("    /** ").append(escapeJavadoc(property.description())).append(" */\n");
            }
            
            classBuilder.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
        }
        
        // Добавляем конструктор
        classBuilder.append("    public ").append(className).append("() {\n");
        classBuilder.append("    }\n\n");
        
        // Добавляем getter и setter методы
        for (YamlNode property : component.schema().properties()) {
            String fieldName = property.name();
            String fieldType = mapType(property.type(), property.rawNode());
            String capitalizedFieldName = capitalizeFirstLetter(fieldName);
            
            // Getter
            classBuilder.append("    public ").append(fieldType).append(" get").append(capitalizedFieldName).append("() {\n");
            classBuilder.append("        return ").append(fieldName).append(";\n");
            classBuilder.append("    }\n\n");
            
            // Setter
            classBuilder.append("    public void set").append(capitalizedFieldName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            classBuilder.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            classBuilder.append("    }\n\n");
        }
        
        // Заканчиваем объявление класса
        classBuilder.append("}");
        
        generatedClasses.add(new GeneratedClass(className, classBuilder.toString()));
        
        // Рекурсивно генерируем классы для вложенных объектов
        for (YamlNode property : component.schema().properties()) {
            if ("object".equals(property.type())) {
                // Создаем временную схему для вложенного объекта
                YamlSchema nestedSchema = new YamlSchema(
                    property.name(),
                    property.description(),
                    property.properties(),
                    property.rawNode()
                );
                
                ComponentSchema nestedComponent = new ComponentSchema(
                    property.name(),
                    nestedSchema,
                    property.rawNode()
                );
                
                generatedClasses.addAll(generateClass(nestedComponent, spec));
            } else if (isReference(property.rawNode())) {
                // Обработка ссылок на компоненты
                String referenceName = getReferenceName(property.rawNode());
                if (referenceName != null) {
                    ComponentSchema referencedComponent = findComponentByName(spec, referenceName);
                    if (referencedComponent != null) {
                        generatedClasses.addAll(generateClass(referencedComponent, spec));
                    }
                }
            }
        }
        
        return generatedClasses;
    }

    /**
     * Собирает необходимые imports для схемы.
     *
     * @param schema схема
     * @return множество imports
     */
    private Set<String> collectImports(YamlSchema schema) {
        Set<String> imports = new HashSet<>();
        
        for (YamlNode property : schema.properties()) {
            String javaType = mapType(property.type(), property.rawNode());
            
            // Добавляем import для коллекций
            if (javaType.startsWith("List<") || javaType.startsWith("Set<") || javaType.startsWith("Map<")) {
                imports.add("java.util." + javaType.substring(0, javaType.indexOf('<')));
            }
        }
        
        return imports;
    }

    /**
     * Преобразует YAML тип в Java тип.
     *
     * @param yamlType тип из YAML
     * @param rawNode  узел YAML
     * @return Java тип
     */
    private String mapType(String yamlType, Map<String, Object> rawNode) {
        if (yamlType == null) {
            return "Object";
        }
        
        // Проверяем, является ли это ссылкой на компонент
        if (isReference(rawNode)) {
            String referenceName = getReferenceName(rawNode);
            if (referenceName != null) {
                return capitalizeFirstLetter(referenceName);
            }
        }
        
        return switch (yamlType) {
            case "string" -> "String";
            case "integer" -> "Integer";
            case "number" -> "Double";
            case "boolean" -> "Boolean";
            case "array" -> {
                // Пытаемся определить тип элементов массива
                Map<String, Object> items = getMapValue(rawNode, "items");
                if (items != null) {
                    String itemType = getStringValue(items, TYPE);
                    if (itemType != null) {
                        yield "List<" + mapType(itemType, items) + ">";
                    }
                }
                yield "List<Object>";
            }
            case "object" -> {
                // Если это объект с определенными свойствами, возвращаем его имя
                if (rawNode.containsKey(TITLE)) {
                    String title = getStringValue(rawNode, TITLE);
                    if (title != null) {
                        yield capitalizeFirstLetter(title);
                    }
                }
                yield "Object";
            }
            default -> "Object";
        };
    }

    /**
     * Находит компонент по имени.
     *
     * @param spec спецификация AsyncAPI
     * @param name имя компонента
     * @return компонент схемы или null
     */
    private ComponentSchema findComponentByName(AsyncApiSpec spec, String name) {
        for (ComponentSchema component : spec.components()) {
            if (component.name().equals(name)) {
                return component;
            }
        }
        return null;
    }

    /**
     * Приводит первую букву строки к заглавной.
     *
     * @param str строка
     * @return строка с заглавной первой буквой
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Экранирует специальные символы в Javadoc.
     *
     * @param str строка для экранирования
     * @return экранированная строка
     */
    private String escapeJavadoc(String str) {
        if (str == null) {
            return str;
        }
        return str.replace("*/", "*&#47;");
    }

    /**
     * Record для представления сгенерированного класса.
     */
    public record GeneratedClass(String className, String content) {
    }
}