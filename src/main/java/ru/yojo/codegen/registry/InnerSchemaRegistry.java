package ru.yojo.codegen.registry;

import ru.yojo.codegen.constants.Dictionary;
import ru.yojo.codegen.util.MapperUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Registry for managing inner (anonymous) schemas discovered during mapping.
 *
 * <p>Inner schemas are schemas that are defined inline within properties
 * (e.g., anonymous objects, enums) and need to be generated as separate classes.
 *
 * <p>This registry:
 * <ul>
 *   <li>Stores discovered inner schemas in insertion order</li>
 *   <li>Determines whether a schema is primitive or needs generation</li>
 *   <li>Ensures unique naming for generated schemas</li>
 *   <li>Provides utility methods for schema registration</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class InnerSchemaRegistry {

    private final Map<String, Object> innerSchemas;

    /**
     * Creates a new InnerSchemaRegistry with an empty LinkedHashMap.
     */
    public InnerSchemaRegistry() {
        this.innerSchemas = new LinkedHashMap<>();
    }

    /**
     * Creates a new InnerSchemaRegistry with the provided map.
     *
     * @param innerSchemas existing map of inner schemas (will be copied)
     */
    public InnerSchemaRegistry(Map<String, Object> innerSchemas) {
        this.innerSchemas = new LinkedHashMap<>(innerSchemas);
    }

    /**
     * Registers an inner schema with the given name and definition.
     *
     * @param schemaName       name of the schema
     * @param schemaDefinition raw schema definition map
     */
    public void register(String schemaName, Map<String, Object> schemaDefinition) {
        Map<String, Object> innerSchema = new LinkedHashMap<>(schemaDefinition);

        // Ensure type is set
        if (!innerSchema.containsKey(TYPE)) {
            innerSchema.put(TYPE, OBJECT);
        }

        // Move non-metadata fields to PROPERTIES if not already present
        if (!innerSchema.containsKey(PROPERTIES) &&
            !innerSchema.containsKey(ENUMERATION) &&
            !innerSchema.containsKey(REFERENCE)) {

            Map<String, Object> actualProps = new LinkedHashMap<>();
            Set<String> metadataKeys = Set.of(
                TYPE, FORMAT, DESCRIPTION, EXAMPLE, DEFAULT, PATTERN,
                MIN_LENGTH, MAX_LENGTH, MINIMUM, MAXIMUM, DIGITS,
                REALIZATION, NAME, PACKAGE
            );

            innerSchema.entrySet().removeIf(entry -> {
                if (!metadataKeys.contains(entry.getKey())) {
                    actualProps.put(entry.getKey(), entry.getValue());
                    return true;
                }
                return false;
            });

            if (!actualProps.isEmpty()) {
                innerSchema.put(PROPERTIES, actualProps);
            }
        }

        innerSchemas.put(schemaName, innerSchema);
    }

    /**
     * Registers an enum schema.
     *
     * @param enumClassName name of the enum class
     * @param propertiesMap raw property map containing enum values
     */
    public void registerEnum(String enumClassName, Map<String, Object> propertiesMap) {
        Map<String, Object> enumSchema = new LinkedHashMap<>();
        enumSchema.put(TYPE, ENUMERATION);
        enumSchema.put(ENUMERATION, propertiesMap.get(ENUMERATION));

        if (propertiesMap.containsKey(X_ENUM_NAMES)) {
            enumSchema.put(X_ENUM_NAMES, propertiesMap.get(X_ENUM_NAMES));
        }

        innerSchemas.put(enumClassName, enumSchema);
    }

    /**
     * Checks if a schema name is already registered.
     *
     * @param schemaName name to check
     * @return true if the schema is already registered
     */
    public boolean contains(String schemaName) {
        return innerSchemas.containsKey(schemaName);
    }

    /**
     * Returns the inner schema definition for the given name.
     *
     * @param schemaName name of the schema
     * @return schema definition map, or null if not found
     */
    public Map<String, Object> get(String schemaName) {
        return (Map<String, Object>) innerSchemas.get(schemaName);
    }

    /**
     * Returns all registered inner schemas.
     *
     * @return map of schema name to schema definition
     */
    public Map<String, Object> getAll() {
        return new LinkedHashMap<>(innerSchemas);
    }

    /**
     * Returns the number of registered inner schemas.
     *
     * @return count of inner schemas
     */
    public int size() {
        return innerSchemas.size();
    }

    /**
     * Checks if the registry is empty.
     *
     * @return true if no inner schemas are registered
     */
    public boolean isEmpty() {
        return innerSchemas.isEmpty();
    }

    /**
     * Determines if a type name represents a primitive type that doesn't need generation.
     *
     * @param typeName the type name to check
     * @return true if the type is primitive (doesn't need DTO generation)
     */
    public static boolean isPrimitiveType(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return true;
        }
        return Set.of(
            "String", "Integer", "Long", "Double", "Float", "Boolean",
            "BigDecimal", "LocalDate", "LocalDateTime", "OffsetDateTime",
            "UUID", "Object", "Void", "Byte", "Short", "Character"
        ).contains(typeName);
    }

    /**
     * Generates a unique schema name to avoid collisions.
     *
     * @param baseName the base name for the schema
     * @return a unique schema name
     */
    public String generateUniqueName(String baseName) {
        String candidate = baseName;
        int counter = 1;
        while (innerSchemas.containsKey(candidate)) {
            candidate = baseName + counter;
            counter++;
        }
        return candidate;
    }

    /**
     * Clears all registered inner schemas.
     */
    public void clear() {
        innerSchemas.clear();
    }
}
