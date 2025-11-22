package ru.croc.yojo.generator.record;

import java.util.List;
import java.util.Map;

/**
 * Record для представления узла YAML.
 * Используется для безопасного и неизменяемого представления данных YAML.
 */
public record YamlNode(
        String name,
        String type,
        String description,
        List<YamlNode> properties,
        Map<String, Object> rawNode
) {
    public YamlNode {
        properties = properties != null ? List.copyOf(properties) : List.of();
        rawNode = rawNode != null ? Map.copyOf(rawNode) : Map.of();
    }
}