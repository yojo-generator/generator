package ru.yojo.codegen_new;

import java.util.List;
import java.util.Map;

/**
 * Record для представления узла YAML
 */
public record YamlNode(
        String type,
        String format,
        String reference,
        Map<String, Object> properties,
        List<Object> items,
        Map<String, Object> additionalProperties,
        List<Object> allOf,
        List<Object> oneOf,
        List<Object> anyOf,
        String description,
        List<String> enumeration,
        Map<String, String> enumNames,
        Map<String, Object> extensions,
        String realization,
        Boolean required,
        String name,
        String packageName
) {
    public YamlNode {
        properties = properties != null ? properties : Map.of();
        items = items != null ? items : List.of();
        additionalProperties = additionalProperties != null ? additionalProperties : Map.of();
        allOf = allOf != null ? allOf : List.of();
        oneOf = oneOf != null ? oneOf : List.of();
        anyOf = anyOf != null ? anyOf : List.of();
        extensions = extensions != null ? extensions : Map.of();
        enumeration = enumeration != null ? enumeration : List.of();
        enumNames = enumNames != null ? enumNames : Map.of();
    }

    public static YamlNode empty() {
        return new YamlNode(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}