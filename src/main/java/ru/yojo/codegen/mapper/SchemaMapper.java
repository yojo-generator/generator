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
                });

                if (needToFill.get()) {
                    schema.setFillParameters(
                            getSchemaVariableProperties(
                                    schemaName,
                                    schemaMap,
                                    processContext.getSchemasMap(),
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    processContext,
                                    processContext.getHelper().getInnerSchemas()
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
                                    processContext.getHelper().getInnerSchemas()
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

        return schemaList;
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
     * @return {@link FillParameters} container with all fields and metadata
     */
    public FillParameters getSchemaVariableProperties(String schemaName,
                                                      Map<String, Object> currentSchema,
                                                      Map<String, Object> schemas,
                                                      Map<String, Object> properties,
                                                      ProcessContext processContext,
                                                      Map<String, Object> innerSchemas) {
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
                        processContext.getHelper().getInnerSchemas());
                variableProperties.add(vp);
            });
        }

        // 🔹 ШАГ 2: Обрабатываем allOf/oneOf/anyOf — и мержим в variableProperties (избегая дублей)
        if (POLYMORPHS.stream().anyMatch(p -> currentSchema.containsKey(p))) {
            System.out.println("POLYMORPH: " + schemaName);
            System.out.println("POLYMORPH: " + currentSchema);
            List<String> polymorphSchemasNames = getPolymorphSchemasNames(currentSchema, schemas);
            System.out.println(polymorphSchemasNames);
            Map<String, Object> mergedProperties = mergeProperties(polymorphSchemasNames, currentSchema, schemas);
            registerNestedSchemas(schemaName, mergedProperties, schemas, processContext.getHelper().getInnerSchemas());
            // ➕ Добавляем НЕДОСТАЮЩИЕ поля из allOf (если их нет в корневых properties)
            mergedProperties.forEach((propertyName, propertyValue) -> {
                if (variableProperties.stream().noneMatch(vp -> vp.getName().equals(propertyName))) {
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
            fillProperties(schemaName, vp, currentSchema, schemas, schemaName, currentSchema, processContext, processContext.getHelper().getInnerSchemas());
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
     * </ul>
     *
     * @param polymorphSchemasNames unused (legacy signature); actual refs resolved from {@code currentSchema}
     * @param currentSchema         schema with {@code allOf}/{@code oneOf}/{@code anyOf}
     * @param schemas               global schema registry
     * @return merged map of property name → definition
     */
    private Map<String, Object> mergeProperties(List<String> polymorphSchemasNames,
                                                Map<String, Object> currentSchema,
                                                Map<String, Object> schemas) {
        Map<String, Object> merged = new LinkedHashMap<>();

        // 1. Сначала обрабатываем allOf/oneOf/anyOf: добавляем свойства из $ref и inline-объектов
        for (String polyKey : POLYMORPHS) {
            if (currentSchema.containsKey(polyKey)) {
                List<Object> items = castObjectToListObjects(currentSchema.get(polyKey));
                for (Object item : items) {
                    if (item instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> itemMap = (Map<String, Object>) item;

                        // Случай 1: $ref → разрешаем и добавляем его properties
                        String ref = getStringValueIfExistOrElseNull(REFERENCE, itemMap);
                        if (ref != null) {
                            String schemaName = refReplace(ref);
                            Map<String, Object> target = castObjectToMap(schemas.get(schemaName));
                            if (target != null) {
                                Map<String, Object> props = castObjectToMap(target.get(PROPERTIES));
                                if (props != null) {
                                    merged.putAll(props);
                                }
                            }
                        }
                        // Случай 2: inline объект с properties
                        else if (OBJECT.equals(getStringValueIfExistOrElseNull(TYPE, itemMap))) {
                            Map<String, Object> props = castObjectToMap(itemMap.get(PROPERTIES));
                            if (props != null) {
                                merged.putAll(props);
                            }
                        }
                        // Случай 3: bare properties (редко, но возможно)
                        else if (itemMap.containsKey(PROPERTIES)) {
                            Map<String, Object> props = castObjectToMap(itemMap.get(PROPERTIES));
                            if (props != null) {
                                merged.putAll(props);
                            }
                        }
                    }
                }
            }
        }

        // 2. Если есть корневые properties — мержим их поверх (приоритет highest)
        //    Это нужно для ExampleFive: allOf + корневые properties
        Map<String, Object> rootProps = castObjectToMap(currentSchema.get(PROPERTIES));
        if (rootProps != null && !rootProps.isEmpty()) {
            merged.putAll(rootProps);
        }

        return merged;
    }
}