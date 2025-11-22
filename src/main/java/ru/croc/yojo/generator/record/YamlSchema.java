package ru.croc.yojo.generator.record;

import java.util.List;
import java.util.Map;

/**
 * Record для представления схемы YAML.
 * Используется для безопасного и неизменяемого представления данных схемы.
 */
public record YamlSchema(
        String title,
        String description,
        List<YamlNode> properties,
        Map<String, Object> rawSchema
) {
    public YamlSchema {
        properties = properties != null ? List.copyOf(properties) : List.of();
        rawSchema = rawSchema != null ? Map.copyOf(rawSchema) : Map.of();
    }
}