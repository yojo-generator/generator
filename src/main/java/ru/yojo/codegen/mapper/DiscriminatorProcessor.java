package ru.yojo.codegen.mapper;

import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.util.Logger;

import java.util.*;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Processes discriminator-based polymorphism for AsyncAPI schemas.
 * <p>
 * Handles:
 * <ul>
 *   <li>Pre-scanning schemas to find base schemas with a {@code discriminator} field</li>
 *   <li>Linking subtypes (via {@code allOf} + {@code $ref}) to their discriminator base</li>
 *   <li>Resolving discriminator values including {@code const} overrides</li>
 *   <li>Marking discriminator fields for {@code @JsonTypeId} annotation</li>
 *   <li>Detecting discriminator fields with {@code const} in subtypes (to skip field generation)</li>
 * </ul>
 * <p>
 * Usage: Call {@link #preScan(Map)} once per specification, then {@link #processDiscriminators(List, Map)}.
 */
public class DiscriminatorProcessor {

    private static final Logger LOG = new Logger(DiscriminatorProcessor.class);

    private final Set<String> discriminatorBases = new HashSet<>();
    private final Map<String, String> discriminatorFieldMap = new HashMap<>();

    // ————————————————————————————————————————
    // Pre-scan
    // ————————————————————————————————————————

    /**
     * Pre-scans all schemas to collect discriminator base schema names and their discriminator field names.
     * Must be called before {@link #processDiscriminators(List, Map)}.
     *
     * @param schemasMap global schema registry (name → definition)
     */
    public void preScan(Map<String, Object> schemasMap) {
        discriminatorBases.clear();
        discriminatorFieldMap.clear();
        schemasMap.forEach((schemaName, schemaValues) -> {
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            if (schemaMap != null && schemaMap.containsKey(DISCRIMINATOR)) {
                String baseName = capitalize(schemaName);
                discriminatorBases.add(baseName);
                String discField = getStringValueIfExistOrElseNull(DISCRIMINATOR, schemaMap);
                discriminatorFieldMap.put(baseName, discField);
            }
        });
    }

    // ————————————————————————————————————————
    // Processing
    // ————————————————————————————————————————

    /**
     * Processes discriminator-based polymorphism for all schemas.
     * <p>
     * For each base schema with a discriminator, finds all subtypes (schemas with {@code allOf} referencing the base),
     * registers them as subtypes (for {@code @JsonSubTypes}), and sets up {@code extends} relationships.
     *
     * @param schemaList list of all {@link Schema} objects
     * @param schemasMap global schema registry (name → definition)
     */
    public void processDiscriminators(List<Schema> schemaList, Map<String, Object> schemasMap) {
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

                    // Set discriminator field on schema
                    schema.setDiscriminator(disc);
                    schema.setDiscriminatorField(disc);

                    // Mark the discriminator field in VariableProperties (for @JsonTypeId)
                    markDiscriminatorFieldInSchema(schema, disc);

                    LOG.info("DISCRIMINATOR: Base schema " + schema.getSchemaName()
                            + " has discriminator field: " + disc);
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
                            String discriminatorField = baseDiscriminator.get(baseName);
                            String discriminatorValue = findDiscriminatorValue(schemaMap, discriminatorField, schemaName);

                            baseSchema.addSubtype(schemaName, discriminatorValue);
                            schema.setExtendsFrom(baseName);

                            LOG.info("DISCRIMINATOR: Setting extendsFrom=\"" + baseName
                                    + "\" for subtype: " + schemaName
                                    + " (discriminator value: " + discriminatorValue + ")");
                        }
                    }
                }
            }
        }
    }

    // ————————————————————————————————————————
    // Lookup helpers
    // ————————————————————————————————————————

    /**
     * Finds the field name that matches the discriminator value by scanning properties.
     *
     * @param properties    map of property name → property definition
     * @param discriminator discriminator value (e.g., {@code "Cat"}, {@code "Dog"})
     * @return field name for {@code @JsonTypeId}, or {@code null} if not found
     */
    public static String findDiscriminatorField(Map<String, Object> properties, String discriminator) {
        if (properties == null || discriminator == null) return null;

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey();
            Map<String, Object> propertyMap = castObjectToMap(entry.getValue());
            if (propertyMap == null) continue;

            Object enumObj = propertyMap.get(ENUMERATION);
            if (enumObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> enumList = (List<Object>) enumObj;
                if (enumList.contains(discriminator)) {
                    return propertyName;
                }
            }

            if (propertyName.equalsIgnoreCase(discriminator)
                    || propertyName.equalsIgnoreCase("type")) {
                return propertyName;
            }
        }

        return properties.containsKey("type") ? "type" : null;
    }

    /**
     * Finds the discriminator field name for a schema by checking its {@code allOf}/{@code oneOf}/{@code anyOf}
     * references against known discriminator bases.
     *
     * @param currentSchema the schema to check
     * @return discriminator field name, or {@code null} if not found
     */
    public String findDiscriminatorFieldForSchema(Map<String, Object> currentSchema) {
        for (String polyKey : POLYMORPHS) {
            if (currentSchema.containsKey(polyKey)) {
                Object polyListObj = currentSchema.get(polyKey);
                if (polyListObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Object> polyList = (List<Object>) polyListObj;
                    for (Object item : polyList) {
                        if (item instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> itemMap = (Map<String, Object>) item;
                            String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                            if (ref != null) {
                                String schemaName = refReplace(ref);
                                if (discriminatorFieldMap.containsKey(schemaName)) {
                                    return discriminatorFieldMap.get(schemaName);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds the discriminator value for a subtype schema.
     * <p>
     * Logic:
     * <ul>
     *   <li>If the discriminator field has a {@code const} value, use it (e.g., {@code StickBug})</li>
     *   <li>Otherwise, use the schema name as the default discriminator value</li>
     * </ul>
     *
     * @param subtypeSchemaMap          the subtype schema definition
     * @param discriminatorField        the name of the discriminator field
     * @param defaultDiscriminatorValue the default value (usually the schema name)
     * @return the discriminator value for {@code @JsonSubTypes.Type(name = "...")}
     */
    public static String findDiscriminatorValue(Map<String, Object> subtypeSchemaMap,
                                                 String discriminatorField,
                                                 String defaultDiscriminatorValue) {
        if (subtypeSchemaMap == null || discriminatorField == null) {
            return defaultDiscriminatorValue;
        }

        // Check inline properties for const value in discriminator field
        Map<String, Object> properties = castObjectToMap(subtypeSchemaMap.get(PROPERTIES));
        if (properties != null) {
            Map<String, Object> discProperty = castObjectToMap(properties.get(discriminatorField));
            if (discProperty != null && discProperty.containsKey(CONST)) {
                Object constValue = discProperty.get(CONST);
                LOG.info("DISCRIMINATOR: Found const value '" + constValue
                        + "' for field '" + discriminatorField + "'");
                return constValue.toString();
            }
        }

        // Check allOf/oneOf/anyOf for inline objects with discriminator field const
        for (String polyKey : POLYMORPHS) {
            if (subtypeSchemaMap.containsKey(polyKey)) {
                List<Object> items = castObjectToListObjects(subtypeSchemaMap.get(polyKey));
                for (Object item : items) {
                    Map<String, Object> itemMap = castObjectToMap(item);
                    if (itemMap != null && OBJECT.equals(getStringValueIfExistOrElseNull(TYPE, itemMap))) {
                        Map<String, Object> inlineProps = castObjectToMap(itemMap.get(PROPERTIES));
                        if (inlineProps != null) {
                            Map<String, Object> discProperty = castObjectToMap(inlineProps.get(discriminatorField));
                            if (discProperty != null && discProperty.containsKey(CONST)) {
                                Object constValue = discProperty.get(CONST);
                                LOG.info("DISCRIMINATOR: Found const value '" + constValue
                                        + "' in " + polyKey + " for field '" + discriminatorField + "'");
                                return constValue.toString();
                            }
                        }
                    }
                }
            }
        }

        return defaultDiscriminatorValue;
    }

    // ————————————————————————————————————————
    // Field marking
    // ————————————————————————————————————————

    /**
     * Marks the field with the given name as discriminator field in the schema's {@link VariableProperties}.
     * This causes the field to be annotated with {@code @JsonTypeId} in the generated code.
     *
     * @param schema                 the schema containing the field
     * @param discriminatorFieldName the discriminator field name
     */
    public static void markDiscriminatorFieldInSchema(Schema schema, String discriminatorFieldName) {
        if (schema.getFillParameters() == null) {
            LOG.info("DISCRIMINATOR WARNING: FillParameters is NULL for schema: " + schema.getSchemaName());
            return;
        }

        if (schema.getFillParameters().getVariableProperties().isEmpty()) {
            LOG.info("DISCRIMINATOR WARNING: VariableProperties is EMPTY for schema: " + schema.getSchemaName());
            return;
        }

        LOG.info("DISCRIMINATOR: Looking for field '" + discriminatorFieldName
                + "' in schema: " + schema.getSchemaName()
                + ", available fields: " + schema.getFillParameters().getVariableProperties().stream()
                .map(VariableProperties::getName)
                .toList());

        for (VariableProperties vp : schema.getFillParameters().getVariableProperties()) {
            if (vp.getName() != null && vp.getName().equals(discriminatorFieldName)) {
                vp.setDiscriminatorField(true);
                LOG.info("DISCRIMINATOR: SUCCESS - Marked field '" + discriminatorFieldName
                        + "' as discriminator field in schema: " + schema.getSchemaName()
                        + " (isDiscriminatorField=" + vp.isDiscriminatorField() + ")");
                break;
            }
        }
    }

    // ————————————————————————————————————————
    // Const detection
    // ————————————————————————————————————————

    /**
     * Checks if a property is the discriminator field with a {@code const} value.
     * Used to skip generating the field in subtypes (it is inherited from the base class).
     *
     * @param currentSchema the schema to check
     * @param propertyName  the property name to check
     * @return {@code true} if this property is a discriminator field with a {@code const} value
     */
    public boolean isDiscriminatorFieldWithConst(Map<String, Object> currentSchema, String propertyName) {
        for (String polyKey : POLYMORPHS) {
            if (!currentSchema.containsKey(polyKey)) continue;

            List<Object> items = castObjectToListObjects(currentSchema.get(polyKey));
            for (Object item : items) {
                Map<String, Object> itemMap = castObjectToMap(item);
                if (itemMap == null) continue;

                String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                if (ref != null) {
                    String baseName = refReplace(ref);
                    if (discriminatorBases.contains(baseName)) {
                        // Check inline properties for const
                        Map<String, Object> props = castObjectToMap(currentSchema.get(PROPERTIES));
                        if (props != null) {
                            Map<String, Object> propDef = castObjectToMap(props.get(propertyName));
                            if (propDef != null && propDef.containsKey(CONST)) {
                                return true;
                            }
                        }

                        // Check inline objects in allOf/oneOf/anyOf
                        for (String polyKey2 : POLYMORPHS) {
                            if (!currentSchema.containsKey(polyKey2)) continue;
                            List<Object> items2 = castObjectToListObjects(currentSchema.get(polyKey2));
                            for (Object item2 : items2) {
                                Map<String, Object> itemMap2 = castObjectToMap(item2);
                                if (itemMap2 != null && OBJECT.equals(getStringValueIfExistOrElseNull(TYPE, itemMap2))) {
                                    Map<String, Object> inlineProps = castObjectToMap(itemMap2.get(PROPERTIES));
                                    if (inlineProps != null) {
                                        Map<String, Object> propDef = castObjectToMap(inlineProps.get(propertyName));
                                        if (propDef != null && propDef.containsKey(CONST)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // ————————————————————————————————————————
    // Accessors
    // ————————————————————————————————————————

    /**
     * Returns an immutable view of discriminator base schema names.
     *
     * @return set of base schema names
     */
    public Set<String> getDiscriminatorBases() {
        return Collections.unmodifiableSet(discriminatorBases);
    }

    /**
     * Returns the discriminator field name for a given base schema.
     *
     * @param baseName base schema name
     * @return discriminator field name, or {@code null} if not a discriminator base
     */
    public String getDiscriminatorField(String baseName) {
        return discriminatorFieldMap.get(baseName);
    }
}
