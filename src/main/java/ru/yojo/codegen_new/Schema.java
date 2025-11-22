package ru.yojo.codegen_new;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Record для представления схемы
 */
public record Schema(
        String schemaName,
        String packageName,
        String description,
        YamlNode yamlNode,
        List<Field> fields,
        Set<String> imports,
        Set<String> interfaces,
        String superClass,
        boolean isEnum,
        Map<String, String> enumValues,
        boolean isInterface,
        Map<String, Object> methods
) {
    public Schema {
        fields = fields != null ? fields : List.of();
        imports = imports != null ? imports : Set.of();
        interfaces = interfaces != null ? interfaces : Set.of();
        enumValues = enumValues != null ? enumValues : Map.of();
        methods = methods != null ? methods : Map.of();
    }
}