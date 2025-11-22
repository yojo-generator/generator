package ru.croc.yojo.generator.record;

import java.util.Map;

/**
 * Record для представления компонента схемы.
 * Используется для безопасного и неизменяемого представления данных компонента.
 */
public record ComponentSchema(
        String name,
        YamlSchema schema,
        Map<String, Object> rawComponent
) {
    public ComponentSchema {
        rawComponent = rawComponent != null ? Map.copyOf(rawComponent) : Map.of();
    }
}