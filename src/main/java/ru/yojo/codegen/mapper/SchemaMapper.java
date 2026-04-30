package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.util.MapperUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LombokUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Mapper responsible for converting AsyncAPI {@code components.schemas} definitions into {@link Schema} objects.
 * <p>
 * Handles:
 * <ul>
 *   <li>Regular DTO classes (with fields, Lombok, validation annotations)</li>
 *   <li>Enums (with or without {@code x-enumNames} descriptions)</li>
 *   <li>Interfaces (marker or with method definitions)</li>
 *   <li>Polymorphic schemas ({@code allOf}, {@code oneOf}, {@code anyOf}) via deep merging</li>
 *   <li>Inner schemas (anonymous objects/arrays inside properties)</li>
 *   <li>Inheritance and interface implementation via {@code extends}/{@code implements}</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class SchemaMapper extends AbstractMapper {

    // ⚡ PRE-SCAN: Set of discriminator base schema names
    private Set<String> discriminatorBases = new HashSet<>();
    
    // ⚡ PRE-SCAN: Map from base schema name to discriminator field name
    private Map<String, String> discriminatorFieldMap = new HashMap<>();

    /**
     * Converts all top-level and inner schemas from the given {@link ProcessContext} into {@link Schema} instances.
     * <p>
     * For each schema:
     * <ul>
     *   <li>Resolves {@code format: interface} → interface generation</li>
     *   <li>Parses Lombok configuration ({@code lombok: {...}})</li>
     *   <li>Applies inheritance/implementation rules ({@code extends}, {@code implements})</li>
     *   <li>Calls {@link #getSchemaVariableProperties} to extract field definitions</li>
     *   <li>Recursively processes {@code innerSchemas} discovered by {@link AbstractMapper}</li>
     * </ul>
     *
     * @param processContext current generation context (schemas, helper, packages, etc.)
     * @return list of fully-populated {@link Schema} objects ready for code generation
     */
    public List<Schema> mapSchemasToObjects(ProcessContext processContext) {
        List<Schema> schemaList = new ArrayList<>();
        
        // ⚡ PRE-SCAN: Collect all discriminator base schemas (use class field!)
        discriminatorBases.clear(); // Clear before filling
        discriminatorFieldMap.clear(); // Clear before filling
        processContext.getSchemasMap().forEach((schemaName, schemaValues) -> {
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            if (schemaMap != null && schemaMap.containsKey(DISCRIMINATOR)) {
                String baseName = capitalize(schemaName);
                discriminatorBases.add(baseName);
                // Store the discriminator field name
                String discField = getStringValueIfExistOrElseNull(DISCRIMINATOR, schemaMap);
                discriminatorFieldMap.put(baseName, discField);
                System.out.println("DISCRIMINATOR PRE-SCAN: Found base schema: " + schemaName + " with discriminator field: " + discField);
            }
        });
        System.out.println("DISCRIMINATOR PRE-SCAN: Discriminator bases: " + discriminatorBases);
        System.out.println("DISCRIMINATOR PRE-SCAN: Discriminator field map: " + discriminatorFieldMap);
        
        processContext.getSchemasMap().forEach((schemaName, schemaValues) -> {
            LombokProperties finalLombokProperties = LombokProperties.newLombokProperties(processContext.getLombokProperties());
            System.out.println("START MAPPING OF SCHEMA: " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            String format = getStringValueIfExistOrElseNull(FORMAT, schemaMap);

            if (format != null && format.equals(INTERFACE)) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setPackageName(processContext.getCommonPackage());
                schema.setFillParameters(new FillParameters(new ArrayList<>()));
                schema.setInterface(true);
                schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                schema.setMethods(castObjectToMap(schemaMap.get(METHODS)));
                schema.setInterfaceImports(getSetValueIfExistsOrElseEmptySet(IMPORTS, schemaMap));
                schemaList.add(schema);
                return;
            }

            if (schemaMap != null && schemaMap.containsKey(LOMBOK)) {
                Map<String, Object> lombokProps = castObjectToMap(schemaMap.get(LOMBOK));
                if (lombokProps.containsKey(ENABLE) &&
                    "false" .equals(getStringValueIfExistOrElseNull(ENABLE, lombokProps))) {
                    finalLombokProperties.setEnableLombok(Boolean.valueOf(getStringValueIfExistOrElseNull(ENABLE, lombokProps)));
                } else {
                    fillLombokAccessors(finalLombokProperties, lombokProps);
                    fillLombokEqualsAndHashCode(finalLombokProperties, lombokProps);
                    fillLombokConstructors(finalLombokProperties, lombokProps);
                }
            }

            if ((schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) || POLYMORPHS.stream().anyMatch(p -> schemaMap.containsKey(p))) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                schema.setLombokProperties(finalLombokProperties);
                schema.setPackageName(processContext.getCommonPackage());

                AtomicBoolean needToFill = new AtomicBoolean(true);
                schemaMap.forEach((sk, sv) -> {
                    if (sk.equals(EXTENDS)) {
                        Map<String, Object> extendsMap = castObjectToMap(sv);
                        String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
                        String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
                        System.out.println("SHOULD EXTENDS FROM: " + fromClass);
                        schema.setExtendsFrom(fromClass);
                        if (fromPackage != null) {
                            schema.getImportSet().add(fromPackage + "." + fromClass + ";");
                        } else {
                            String effectivePkg = processContext.getEffectiveCommonPackage().replace(";", "");
                            schema.getImportSet().add(effectivePkg + "." + fromClass + ";");
                        }
                        String refObject = getStringValueIfExistOrElseNull(REFERENCE, schemaMap);
                        if (refObject != null && refReplace(refObject).equals(fromClass)) {
                            needToFill.set(false);
                        }
                    }
                    if (sk.equals(IMPLEMENTS)) {
                        Map<String, Object> implementsMap = castObjectToMap(sv);
                        List<String> fromInterfaceList = castObjectToList(implementsMap.get(FROM_INTERFACE));
                        System.out.println("SHOULD IMPLEMENTS FROM: " + fromInterfaceList);
                        fromInterfaceList.forEach(ifc -> {
                            String[] split = ifc.split("[.]");
                            schema.getImplementsFrom().add(split[split.length - 1]);
                            schema.getImportSet().add(ifc + ";");
                        });
                    }
                    // Process x-class-annotation
                    if (sk.equals(X_CLASS_ANNOTATION)) {
                        Set<String> classAnnotations = getSetValueIfExistsOrElseEmptySet(X_CLASS_ANNOTATION, schemaMap);
                        if (!classAnnotations.isEmpty()) {
                            schema.getClassAnnotations().addAll(classAnnotations);
                        }
                    }
                });

                if (needToFill.get()) {
                    schema.setFillParameters(
                            getSchemaVariableProperties(
                                    schemaName,
                                    schemaMap,
                                    processContext.getSchemasMap(),
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    processContext,
                                    processContext.getHelper().getInnerSchemas(),
                                    discriminatorBases
                            )
                    );
                } else {
                    schema.setFillParameters(new FillParameters(new ArrayList<>()));
                }
                schemaList.add(schema);
            } else if (schemaType != null && JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                System.out.println("SKIP SCHEMA BECAUSE TYPE IS: " + schemaType);
            } else {
                throw new SchemaFillException("NOT DEFINED TYPE OF SCHEMA! Schema: " + schemaName);
            }
        });

        if (!processContext.getHelper().getInnerSchemas().isEmpty()) {
            processContext.getHelper().getInnerSchemas().forEach((schemaName, schemaValues) -> {
                LombokProperties finalLombokProperties = LombokProperties.newLombokProperties(processContext.getLombokProperties());
                System.out.println("START MAPPING OF INNER SCHEMA: " + schemaName);
                Map<String, Object> schemaMap = castObjectToMap(schemaValues);

                // ⬇️ infer type: object
                String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
                if (schemaType == null) {
                    if (schemaMap.containsKey(PROPERTIES) ||
                        schemaMap.containsKey(ENUMERATION) ||
                        schemaMap.containsKey(REFERENCE) ||
                        POLYMORPHS.stream().anyMatch(schemaMap::containsKey)) {
                        schemaType = OBJECT;
                        schemaMap.put(TYPE, OBJECT);
                        System.out.println("  → inferred missing 'type' as 'object'");
                    }
                }

                if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                    Schema schema = new Schema();
                    schema.setSchemaName(capitalize(schemaName));
                    schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                    schema.setLombokProperties(finalLombokProperties);
                    schema.setPackageName(processContext.getCommonPackage());
                    schema.setFillParameters(
                            getSchemaVariableProperties(
                                    schemaName,
                                    schemaMap,
                                    processContext.getHelper().getInnerSchemas(),
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    processContext,
                                    processContext.getHelper().getInnerSchemas(),
                                    discriminatorBases
                            )
                    );
                    schemaList.add(schema);
                } else if (schemaType != null && JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                    System.out.println("SKIP INNER SCHEMA (primitive): " + schemaName + ", type=" + schemaType);
                } else {
                    System.out.println("SKIP INNER SCHEMA (no type + no props/enum/ref): " + schemaName);
                }
            });
        }

        // Process discriminator-based polymorphism
        processDiscriminators(schemaList, processContext.getSchemasMap());

        return schemaList;
    }

    /**
     * Processes discriminator-based polymorphism.
     * <p>
     * Scans all schemas to find:
     * <ul>
     *   <li>Base schemas with {@code discriminator} field — marks them as polymorphic roots</li>
     *   <li>Subtypes via {@code allOf} with {@code $ref} to base schema — adds them to subtypes list</li>
     * </ul>
     * <p>
     * Result: base schema gets @JsonTypeInfo + @JsonSubTypes annotations.
     * <p>
     * Supports {@code const} in discriminator field to override the default discriminator value (schema name).
     */
    private void processDiscriminators(List<Schema> schemaList, Map<String, Object> schemasMap) {
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
                    schema.setDiscriminator(disc);
                    schemaByName.put(schema.getSchemaName(), schema);
                    
                    // ⚡ SIMPLIFIED: discriminator value IS the field name
                    // In AsyncAPI: discriminator: petType means the field "petType" is the discriminator
                    schema.setDiscriminatorField(disc);
                    System.out.println("DISCRIMINATOR: Base schema " + schema.getSchemaName() + 
                                      " has discriminator field: " + disc);
                }
            }
        }

        // Find subtypes
        for (Schema schema : schemaList) {
            String schemaName = schema.getSchemaName();
            if (baseDiscriminator.containsKey(schemaName)) continue;
            
            String key = capitalize(schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemasMap.get(key));
            if (schemaMap == null) continue;
            
            // Check allOf, oneOf, anyOf for $ref to base schema
            for (String polyKey : POLYMORPHS) {
                if (!schemaMap.containsKey(polyKey)) continue;
                
                List<Object> polyList = castObjectToListObjects(schemaMap.get(polyKey));
                for (Object item : polyList) {
                    Map<String, Object> itemMap = castObjectToMap(item);
                    if (itemMap == null) continue;
                    String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                    if (ref == null) continue;
                    String baseName = refReplace(ref);
                    if (baseDiscriminator.containsKey(baseName)) {
                        Schema baseSchema = schemaByName.get(baseName);
                        if (baseSchema != null) {
                            // Determine the discriminator value for this subtype
                            // Default: schema name; Override: if discriminator field has `const` value
                            String discriminatorField = baseDiscriminator.get(baseName);
                            String discriminatorValue = findDiscriminatorValue(schemaMap, discriminatorField, schemaName);
                            
                            // Add subtype with explicit discriminator value
                            baseSchema.addSubtype(schemaName, discriminatorValue);
                            
                            // ⚡ Set extendsFrom for discriminator-based inheritance
                            schema.setExtendsFrom(baseName);
                            System.out.println("DISCRIMINATOR: Setting extendsFrom=" + baseName + " for subtype: " + schemaName + 
                                              " (discriminator value: " + discriminatorValue + ")");
                            
                            // Mark discriminator field in BASE schema (where @JsonTypeId will be added)
                            if (discriminatorField != null) {
                                baseSchema.setDiscriminatorField(discriminatorField);
                                // Mark the field in the base schema's VariableProperties
                                markDiscriminatorFieldInSchema(baseSchema, discriminatorField);
                                System.out.println("DISCRIMINATOR: Marked discriminator field '" + discriminatorField + "' in BASE class: " + baseName);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Marks the field with the given name as discriminator field in the schema's VariableProperties.
     * 
     * @param schema the schema to update
     * @param discriminatorFieldName the name of the discriminator field
     */
    private void markDiscriminatorFieldInSchema(Schema schema, String discriminatorFieldName) {
        if (schema.getFillParameters() == null) return;
        
        for (VariableProperties vp : schema.getFillParameters().getVariableProperties()) {
            if (vp.getName() != null && vp.getName().equals(discriminatorFieldName)) {
                vp.setDiscriminatorField(true);
                break;
            }
        }
    }
    
    /**
     * Capitalize first letter.
     */
    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Find the field name that matches the discriminator value.
     * Looks through properties to find a field whose value matches the discriminator.
     *
     * @param properties map of property name → property definition
     * @param discriminator discriminator value (e.g., "Cat", "Dog")
     * @return field name that should be annotated with @JsonTypeId, or null if not found
     */
    private static String findDiscriminatorField(Map<String, Object> properties, String discriminator) {
        if (properties == null || discriminator == null) return null;
        
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey();
            Map<String, Object> propertyMap = castObjectToMap(entry.getValue());
            if (propertyMap == null) continue;
            
            // Check if this property has enum with discriminator value
            Object enumObj = propertyMap.get(ENUMERATION);
            if (enumObj instanceof List) {
                List<Object> enumList = (List<Object>) enumObj;
                if (enumList.contains(discriminator)) {
                    return propertyName;
                }
            }
            
            // Also check if property name matches discriminator (common case)
            if (propertyName.equalsIgnoreCase(discriminator) || 
                propertyName.equalsIgnoreCase("type")) {
                return propertyName;
            }
        }
        
        // Default: return "type" or discriminator field name
        return properties.containsKey("type") ? "type" : null;
    }
    
    /**
     * Finds the discriminator field for a schema by looking at its $ref to a discriminator base.
     * If this schema has allOf with $ref to a base that has a discriminator, return that discriminator field.
     * 
     * @param currentSchema the schema to check
     * @return the discriminator field name, or null if not found
     */
    private String findDiscriminatorFieldForSchema(Map<String, Object> currentSchema) {
        for (String polyKey : POLYMORPHS) {
            if (currentSchema.containsKey(polyKey)) {
                List<Object> items = castObjectToListObjects(currentSchema.get(polyKey));
                for (Object item : items) {
                    if (item instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> itemMap = (Map<String, Object>) item;
                        String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                        if (ref != null) {
                            String schemaName = refReplace(ref);
                            // Check if this is a discriminator base schema
                            if (discriminatorFieldMap.containsKey(schemaName)) {
                                return discriminatorFieldMap.get(schemaName);
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
     * @param subtypeSchemaMap the subtype schema definition
     * @param discriminatorField the name of the discriminator field (e.g., "petType")
     * @param defaultDiscriminatorValue the default value (usually the schema name)
     * @return the discriminator value to use in @JsonSubTypes.Type(name = "...")
     */
    private String findDiscriminatorValue(Map<String, Object> subtypeSchemaMap, String discriminatorField, String defaultDiscriminatorValue) {
        if (subtypeSchemaMap == null || discriminatorField == null) {
            return defaultDiscriminatorValue;
        }

        // Check inline properties for const value in discriminator field
        Map<String, Object> properties = castObjectToMap(subtypeSchemaMap.get(PROPERTIES));
        if (properties != null) {
            Map<String, Object> discProperty = castObjectToMap(properties.get(discriminatorField));
            if (discProperty != null && discProperty.containsKey(CONST)) {
                Object constValue = discProperty.get(CONST);
                System.out.println("DISCRIMINATOR: Found const value '" + constValue + "' for field '" + discriminatorField + "'");
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
                                System.out.println("DISCRIMINATOR: Found const value '" + constValue + "' in " + polyKey + " for field '" + discriminatorField + "'");
                                return constValue.toString();
                            }
                        }
                    }
                }
            }
        }

        return defaultDiscriminatorValue;
    }

    /**
     * Checks if a property is the discriminator field with a const value.
     * This is used to skip generating the field in subtypes (it's inherited from base class).
     *
     * @param currentSchema the schema to check
     * @param propertyName the property name to check
     * @param discriminatorBases set of discriminator base schema names
     * @return true if this property is a discriminator field with const value
     */
    private boolean isDiscriminatorFieldWithConst(Map<String, Object> currentSchema, String propertyName, Set<String> discriminatorBases) {
        // Check if this schema has allOf with $ref to a discriminator base
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
                        // This is a subtype of a discriminator base
                        // Check if propertyName is the discriminator field with const value
                        Map<String, Object> props = castObjectToMap(currentSchema.get(PROPERTIES));
                        if (props != null) {
                            Map<String, Object> propDef = castObjectToMap(props.get(propertyName));
                            if (propDef != null && propDef.containsKey(CONST)) {
                                return true;
                            }
                        }
                        
                        // Also check inline objects in allOf/oneOf/anyOf
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

    /**
     * Populates {@link VariableProperties} for a single schema, resolving:
     * <ul>
     *   <li>Top-level {@code properties} (always processed first)</li>
     *   <li>Polymorphic composition ({@code allOf}/{@code oneOf}/{@code anyOf}) — merged <i>after</i> root properties (higher priority for root)</li>
     *   <li>Top-level enums (rare; when enum is directly on schema, not in a property)</li>
     * </ul>
     * <p>
     * Ensures no duplicate fields when merging polymorphic schemas (keeps first occurrence).
     *
     * @param schemaName     schema name (used for logging and inner schema naming)
     * @param currentSchema  definition of the current schema
     * @param schemas        global schema registry for $ref resolution
     * @param properties     root {@code properties} map (may be empty)
     * @param processContext generation context (packages, Spring Boot version, helper)
     * @param innerSchemas   accumulator for discovered inner schemas (mutated)
     * @param discriminatorBases set of discriminator base schema names to skip during merge
     * @return {@link FillParameters} container with all fields and metadata
     */
    public FillParameters getSchemaVariableProperties(String schemaName,
                                                       Map<String, Object> currentSchema,
                                                       Map<String, Object> schemas,
                                                       Map<String, Object> properties,
                                                       ProcessContext processContext,
                                                       Map<String, Object> innerSchemas,
                                                       Set<String> discriminatorBases) {
        List<VariableProperties> variableProperties = new LinkedList<>();

        // 🔹 ШАГ 1: ВСЕГДА обрабатываем корневые properties (если есть)
        if (!properties.isEmpty()) {
            properties.forEach((propertyName, propertyValue) -> {
                VariableProperties vp = new VariableProperties();
                String javaName = MapperUtil.toValidJavaFieldName(propertyName);
                fillProperties(
                        schemaName,
                        vp,
                        currentSchema,
                        schemas,
                        javaName,
                        castObjectToMap(propertyValue),
                        processContext,
                        innerSchemas);
                
                variableProperties.add(vp);
            });
        }

        // 🔹 ШАГ 2: Обрабатываем allOf/oneOf/anyOf — и мержим в variableProperties (избегая дублей)
        if (POLYMORPHS.stream().anyMatch(p -> currentSchema.containsKey(p))) {
            System.out.println("POLYMORPH: " + schemaName);
            System.out.println("POLYMORPH: " + currentSchema);
            List<String> polymorphSchemasNames = getPolymorphSchemasNames(currentSchema, schemas);
            System.out.println(polymorphSchemasNames);
            
            Map<String, Object> mergedProperties = mergeProperties(polymorphSchemasNames, currentSchema, schemas, discriminatorBases);
            registerNestedSchemas(schemaName, mergedProperties, schemas, processContext.getHelper().getInnerSchemas());
            // ➕ Добавляем НЕДОСТАЮЩИЕ поля из allOf (если их нет в корневых properties)
            mergedProperties.forEach((propertyName, propertyValue) -> {
                if (variableProperties.stream().noneMatch(vp -> vp.getName().equals(propertyName))) {
                    // ⚡ SKIP discriminator field with const in subtypes
                    // If this is a discriminator field with const value, skip it - it's inherited from base
                    if (isDiscriminatorFieldWithConst(currentSchema, propertyName, discriminatorBases)) {
                        System.out.println("SKIPPING discriminator field with const: " + propertyName + " in schema: " + schemaName);
                        return;
                    }
                    
                    VariableProperties vp = new VariableProperties();
                    fillProperties(
                            schemaName,
                            vp,
                            currentSchema,
                            schemas,
                            propertyName,
                            castObjectToMap(propertyValue),
                            processContext,
                            processContext.getHelper().getInnerSchemas());

                    variableProperties.add(vp);
                }
            });
        }

        // 🔹 ШАГ 3: Поддержка top-level enum (редко, но бывает)
        else if (getStringValueIfExistOrElseNull(ENUMERATION, currentSchema) != null) {
            VariableProperties vp = new VariableProperties();
            vp.setValid(false);
            vp.setEnum(true);
            fillProperties(schemaName, vp, currentSchema, schemas, schemaName, currentSchema, processContext, innerSchemas);
            variableProperties.add(vp);
        }

        return new FillParameters(variableProperties);
    }

    /**
     * Recursively collects all schema names referenced via {@code $ref} within {@code allOf}/{@code oneOf}/{@code anyOf}.
     * <p>
     * Handles nested polymorphism (e.g., {@code A → allOf → B → oneOf → C}).
     *
     * @param currentSchema current schema map
     * @param schemas       global schema registry
     * @return list of unique schema names (simple, not qualified)
     */
    private static List<String> getPolymorphSchemasNames(Map<String, Object> currentSchema, Map<String, Object> schemas) {
        return POLYMORPHS.stream()
                .filter(p -> currentSchema.containsKey(p))
                .flatMap(p -> castObjectToListObjects(currentSchema.get(p)).stream())
                .map(p -> castObjectToMap(p))
                .map(p -> p.get(REFERENCE))
                .filter(Objects::nonNull)
                .map(p -> refReplace(p.toString()))
                .flatMap(ref -> {
                    Map<String, Object> referencedSchema = castObjectToMap(schemas.get(ref));
                    if (referencedSchema.containsKey(ALL_OF) ||
                        referencedSchema.containsKey(ANY_OF) ||
                        referencedSchema.containsKey(ONE_OF)) {
                        List<String> nestedNames = getPolymorphSchemasNames(referencedSchema, schemas);
                        nestedNames.add(ref);
                        return nestedNames.stream();
                    }
                    return List.of(ref).stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * Merges properties from all schemas involved in {@code allOf}/{@code oneOf}/{@code anyOf}.
     * <p>
     * Rules:
     * <ul>
     *   <li>Properties from {@code $ref} and inline objects ({@code type: object, properties: {...}}) are merged</li>
     *   <li>Root {@code properties} (outside polymorphic block) have highest priority and overwrite duplicates</li>
     *   <li>For discriminator base schemas, ALL fields are skipped - inherited via 'extends'</li>
     * </ul>
     *
     * @param polymorphSchemasNames unused (legacy signature); actual refs resolved from {@code currentSchema}
     * @param currentSchema         schema with {@code allOf}/{@code oneOf}/{@code anyOf}
     * @param schemas               global schema registry
     * @param discriminatorBases    set of discriminator base schema names to skip during merge
     * @return merged map of property name → definition
     */
    private Map<String, Object> mergeProperties(List<String> polymorphSchemasNames,
                                                  Map<String, Object> currentSchema,
                                                  Map<String, Object> schemas,
                                                  Set<String> discriminatorBases) {
        Map<String, Object> merged = new LinkedHashMap<>();
        
        System.out.println("MERGE: Starting merge for schema with keys: " + currentSchema.keySet());
        System.out.println("MERGE: Discriminator bases: " + discriminatorBases);
        
        // 1. Сначала обрабатываем allOf/oneOf/anyOf: добавляем свойства из $ref и inline-объектов
        for (String polyKey : POLYMORPHS) {
            if (currentSchema.containsKey(polyKey)) {
                System.out.println("MERGE: Processing " + polyKey + " for schema");
                List<Object> items = castObjectToListObjects(currentSchema.get(polyKey));
                for (Object item : items) {
                    if (item instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> itemMap = (Map<String, Object>) item;
                        
                        // Случай 1: $ref → разрешаем и добавляем его properties
                        String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                        if (ref != null) {
                            String schemaName = refReplace(ref);
                            System.out.println("MERGE: Found $ref: " + schemaName);
                            
                            // ⚡ CRITICAL: Skip if this is a discriminator base schema
                            if (discriminatorBases.contains(schemaName)) {
                                System.out.println("MERGE: SKIPPING " + schemaName + " - it's a discriminator base, fields will be inherited via 'extends'");
                            } else {
                                Map<String, Object> target = castObjectToMap(schemas.get(schemaName));
                                if (target != null) {
                                    Map<String, Object> props = castObjectToMap(target.get(PROPERTIES));
                                    if (props != null) {
                                        merged.putAll(props);
                                        System.out.println("MERGE: Added properties from " + schemaName + ": " + props.keySet());
                                    } else {
                                        System.out.println("MERGE: WARNING - No properties found in " + schemaName);
                                    }
                                }
                            }
                        }
                        // Случай 2: inline объект с properties
                        else if (OBJECT.equals(getStringValueIfExistOrElseNull(TYPE, itemMap))) {
                            System.out.println("MERGE: Found inline object");
                            Map<String, Object> props = castObjectToMap(itemMap.get(PROPERTIES));
                            if (props != null) {
                                merged.putAll(props);
                                System.out.println("MERGE: Added inline properties: " + props.keySet());
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("MERGE: After processing allOf/oneOf/anyOf, merged keys: " + merged.keySet());
        
        // 2. Если есть корневые properties — мержим их поверх (приоритет highest)
        Map<String, Object> rootProps = castObjectToMap(currentSchema.get(PROPERTIES));
        if (rootProps != null && !rootProps.isEmpty()) {
            merged.putAll(rootProps);
            System.out.println("MERGE: Added root properties: " + rootProps.keySet());
        }
        
        System.out.println("MERGE: Final merged keys: " + merged.keySet());
        return merged;
    }

    /**
     * Returns the set of discriminator base schema names collected during pre-scan.
     * Used by {@link MessageMapper} to skip fields from discriminator base schemas.
     *
     * @return immutable view of discriminator base schema names
     */
    public Set<String> getDiscriminatorBases() {
        return Collections.unmodifiableSet(discriminatorBases);
    }
}