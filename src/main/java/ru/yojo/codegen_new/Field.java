package ru.yojo.codegen_new;

import java.util.Set;

/**
 * Record для представления поля
 */
public record Field(
        String name,
        String type,
        String javaType,
        String description,
        boolean required,
        boolean isEnum,
        boolean isArray,
        boolean isMap,
        String keyType,
        String valueType,
        Set<String> imports,
        String defaultValue
) {
    public Field {
        imports = imports != null ? imports : Set.of();
    }
}