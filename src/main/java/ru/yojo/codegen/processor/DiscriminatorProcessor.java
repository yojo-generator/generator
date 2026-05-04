package ru.yojo.codegen.processor;

import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.util.MapperUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Processor for discriminator-based polymorphism.
 * Centralizes all logic related to:
 * <ul>
 *   <li>Scanning base schemas with {@code discriminator} field</li>
 *   <li>Finding subtypes via {@code allOf} with {@code $ref}</li>
 *   <li>Setting {@code @JsonTypeInfo} / {@code @JsonSubTypes} annotations</li>
 *   <li>Handling {@code const} values in discriminator fields</li>
 * </ul>
 *
 * <p>This extracts complex polymorphism logic from {@link ru.yojo.codegen.mapper.SchemaMapper}
 * into a dedicated, testable component.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class DiscriminatorProcessor {

    // Pre-scan: Set of discriminator base schema names
    private final Set<String> discriminatorBases = new HashSet<>();

    // Pre-scan: Map from base schema name → discriminator field name
    private final Map<String, String> discriminatorFieldMap = new HashMap<>();

    /**
     * Performs pre-scan of schemas to collect discriminator base schemas.
     * Should be called before {@link #process(List, Map)}.
     *
     * @param schemasMap global map of all schemas
     */
    public void preScanDiscriminators(Map<String, Object> schemasMap) {
        discriminatorBases.clear();
        discriminatorFieldMap.clear();

        schemasMap.forEach((schemaName, schemaValues) -> {
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            if (schemaMap != null && schemaMap.containsKey(DISCRIMINATOR)) {
                String baseName = capitalize(schemaName);
                discriminatorBases.add(baseName);
                String discField = getStringValueIfExistOrElseNull(DISCRIMINATOR, schemaMap);
                discriminatorFieldMap.put(baseName, discField);
                System.out.println("DISCRIMINATOR PRE-SCAN: Found base schema: " + schemaName +
                        " with discriminator field: " + discField);
            }
        });

        System.out.println("DISCRIMINATOR PRE-SCAN: Discriminator bases: " + discriminatorBases);
        System.out.println("DISCRIMINATOR PRE-SCAN: Discriminator field map: " + discriminatorFieldMap);
    }

    /**
     * Processes all schemas to add discriminator annotations and set up inheritance.
     *
     * @param schemaList list of {@link Schema} objects to process
     * @param schemasMap global map of all schemas (for $ref resolution)
     */
    public void process(List<Schema> schemaList, Map<String, Object> schemasMap) {
        // Clear existing subtypes to avoid duplication
        for (Schema schema : schemaList) {
            schema.clearSubtypes();
        }

        Map<String, String> baseDiscriminator = new HashMap<>();
        Map<String, Schema> schemaByName = new HashMap<>();

        // Find base schemas with discriminator
        for (Schema schema : schemaList) {
            String key = capitalize(schema.getSchemaName());
            Map<String, Object> schemaMap = castObjectToMap(schemasMap.get(key));
            if (schemaMap != null && schemaMap.containsKey(DISCRIMINATOR)) {
                String disc = getStringValueIfExistOrElseNull(DISCRIMINATOR, schemaMap);
                if (disc != null && !disc.isEmpty()) {
                    baseDiscriminator.put(schema.getSchemaName(), disc);
                    schemaByName.put(schema.getSchemaName(), schema);

                    // Set discriminator field
                    schema.setDiscriminator(disc);
                    schema.setDiscriminatorField(disc);
                    
                    // Mark the discriminator field in VariableProperties (for @JsonTypeId)
                    markDiscriminatorFieldInSchema(schema, disc);
                    
                    System.out.println("DISCRIMINATOR: Base schema " + schema.getSchemaName() +
                            " has discriminator field: " + disc);
                }
            }
        }

        // Find subtypes and set up inheritance
        for (Schema schema : schemaList) {
            String schemaName = schema.getSchemaName();
            if (baseDiscriminator.containsKey(schemaName)) continue; // Skip base schemas

            String key = capitalize(schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemasMap.get(key));

            if (schemaMap == null) continue;

            // Check allOf for $ref to base schema
            Object allOfObj = schemaMap.get(ALL_OF);
            if (allOfObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> allOfList = (List<Object>) allOfObj;

                for (Object item : allOfList) {
                    String ref = getStringValueIfExistOrElseNull(REFERENCE, castObjectToMap(item));
                    if (ref == null) continue;

                    String baseName = refReplace(ref);
                    if (baseDiscriminator.containsKey(baseName)) {
                        Schema baseSchema = schemaByName.get(baseName);
                        if (baseSchema != null) {
                            // Determine discriminator value for this subtype
                            String discriminatorField = baseDiscriminator.get(baseName);
                            String discriminatorValue = findDiscriminatorValue(schemaMap, discriminatorField, schemaName);

                            // Add subtype with explicit discriminator value
                            baseSchema.addSubtype(schemaName, discriminatorValue);

                            // Set extendsFrom for discriminator-based inheritance
                            schema.setExtendsFrom(baseName);

                            System.out.println("DISCRIMINATOR: Setting extendsFrom=\"" + baseName +
                                    "\" for subtype: " + schemaName +
                                    " (discriminator value: " + discriminatorValue + ")");
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds the discriminator value for a subtype.
     * If the discriminator field has a {@code const} value, use it; otherwise default to schema name.
     * <p>
     * Checks both top-level properties and properties inside {@code allOf} items.
     *
     * @param schemaMap         the subtype's schema map
     * @param discriminatorField the discriminator field name
     * @param schemaName        the subtype's name
     * @return the discriminator value to use in {@code @JsonSubTypes.Type}
     */
    private String findDiscriminatorValue(Map<String, Object> schemaMap, String discriminatorField, String schemaName) {
        // Default: schema name
        String discriminatorValue = schemaName;

        // Check if discriminator field has a const value in top-level properties
        discriminatorValue = findConstValueInProperties(
                castObjectToMap(schemaMap.get(PROPERTIES)), 
                discriminatorField,
                discriminatorValue
        );

        // Also check inside allOf items (for cases like StickInsect where const is inside allOf)
        if (discriminatorValue.equals(schemaName) && schemaMap.containsKey(ALL_OF)) {
            Object allOfObj = schemaMap.get(ALL_OF);
            if (allOfObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> allOfList = (List<Object>) allOfObj;
                for (Object item : allOfList) {
                    Map<String, Object> itemMap = castObjectToMap(item);
                    if (itemMap != null && itemMap.containsKey(PROPERTIES)) {
                        discriminatorValue = findConstValueInProperties(
                                castObjectToMap(itemMap.get(PROPERTIES)),
                                discriminatorField,
                                discriminatorValue
                        );
                        if (!discriminatorValue.equals(schemaName)) {
                            break; // Found const value
                        }
                    }
                }
            }
        }

        return discriminatorValue;
    }

    /**
     * Helper method to find const value in a properties map.
     */
    private String findConstValueInProperties(Map<String, Object> props, String discriminatorField, String currentValue) {
        if (props != null && discriminatorField != null && props.containsKey(discriminatorField)) {
            Map<String, Object> fieldDef = castObjectToMap(props.get(discriminatorField));
            if (fieldDef != null && fieldDef.containsKey(CONST)) {
                Object constValue = fieldDef.get(CONST);
                if (constValue != null) {
                    String newValue = constValue.toString();
                    System.out.println("DISCRIMINATOR: Found const value: " + newValue +
                            " for field: " + discriminatorField);
                    return newValue;
                }
            }
        }
        return currentValue;
    }

    /**
     * Marks the field with the given name as discriminator field in the schema's VariableProperties.
     * This will cause the field to be annotated with @JsonTypeId in the generated code.
     *
     * @param schema the schema to update
     * @param discriminatorFieldName the name of the discriminator field
     */
    private void markDiscriminatorFieldInSchema(Schema schema, String discriminatorFieldName) {
        if (schema.getFillParameters() == null) {
            System.out.println("DISCRIMINATOR WARNING: FillParameters is NULL for schema: " + schema.getSchemaName());
            return;
        }
        
        if (schema.getFillParameters().getVariableProperties().isEmpty()) {
            System.out.println("DISCRIMINATOR WARNING: VariableProperties is EMPTY for schema: " + schema.getSchemaName());
            return;
        }
        
        System.out.println("DISCRIMINATOR: Looking for field '" + discriminatorFieldName + 
                "' in schema: " + schema.getSchemaName() + 
                ", available fields: " + schema.getFillParameters().getVariableProperties().stream()
                    .map(VariableProperties::getName)
                    .toList());
        
        for (VariableProperties vp : schema.getFillParameters().getVariableProperties()) {
            if (vp.getName() != null && vp.getName().equals(discriminatorFieldName)) {
                vp.setDiscriminatorField(true);
                System.out.println("DISCRIMINATOR: SUCCESS - Marked field '" + discriminatorFieldName + 
                        "' as discriminator field in schema: " + schema.getSchemaName() +
                        " (isDiscriminatorField=" + vp.isDiscriminatorField() + ")");
                break;
            }
        }
    }

    /**
     * Returns the set of discriminator base schema names (for checking if a schema is a base).
     *
     * @return set of base schema names
     */
    public Set<String> getDiscriminatorBases() {
        return discriminatorBases;
    }

    /**
     * Returns the map from base schema name to discriminator field name.
     *
     * @return map of discriminator fields
     */
    public Map<String, String> getDiscriminatorFieldMap() {
        return discriminatorFieldMap;
    }

    /**
     * Checks if a schema is a discriminator base.
     *
     * @param schemaName schema name to check
     * @return true if it's a base schema with discriminator
     */
    public boolean isDiscriminatorBase(String schemaName) {
        return discriminatorBases.contains(capitalize(schemaName));
    }
}
