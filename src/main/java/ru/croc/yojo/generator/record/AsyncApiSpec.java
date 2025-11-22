package ru.croc.yojo.generator.record;

import java.util.List;
import java.util.Map;

/**
 * Record для представления AsyncAPI спецификации.
 * Используется для безопасного и неизменяемого представления данных спецификации.
 */
public record AsyncApiSpec(
        String asyncapi,
        String id,
        String title,
        String version,
        List<ComponentSchema> components,
        Map<String, Object> rawSpec
) {
    public AsyncApiSpec {
        components = components != null ? List.copyOf(components) : List.of();
        rawSpec = rawSpec != null ? Map.copyOf(rawSpec) : Map.of();
    }
}