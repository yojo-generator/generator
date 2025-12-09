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
 * Maps AsyncAPI schema definitions ({@code components.schemas.*}) to {@link Schema} objects.
 * <p>
 * Supports:
 * <ul>
 *   <li>Regular DTO classes (with fields, Lombok, validation)</li>
 *   <li>Enums (with/without descriptions)</li>
 *   <li>Interfaces (marker or with method definitions)</li>
 *   <li>Polymorphism ({@code oneOf}, {@code allOf}, {@code anyOf})</li>
 *   <li>Inheritance ({@code extends}, {@code implements})</li>
 *   <li>Inner schemas (e.g., nested enums, DTOs)</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class SchemaMapper extends AbstractMapper {

    /**
     * Transforms all schemas in the context into {@link Schema} instances.
     * <p>
     * For each schema:
     * <ul>
     *   <li>Skips primitive-type schemas (e.g., {@code type: string})</li>
     *   <li>Processes interfaces (if {@code format: interface})</li>
     *   <li>Handles inheritance, Lombok config, and field filling</li>
     *   <li>Recursively processes inner schemas (e.g., from enums or polymorphic resolution)</li>
     * </ul>
     *
     * @param processContext generation context
     * @return list of fully prepared {@link Schema} instances
     */
    public List<Schema> mapSchemasToObjects(ProcessContext processContext) {
        processContext.getHelper().setIsMappedFromSchemas(true);
        List<Schema> schemaList = new ArrayList<>();
        processContext.getSchemasMap().forEach((schemaName, schemaValues) -> {
            LombokProperties finalLombokProperties = LombokProperties.newLombokProperties(processContext.getLombokProperties());
            System.out.println("START MAPPING OF SCHEMA: " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            String format = getStringValueIfExistOrElseNull(FORMAT, schemaMap);
            // Interface marker (format: interface)
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
                    "false".equals(getStringValueIfExistOrElseNull(ENABLE, lombokProps))) {
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

                // Handle extends/implements early to determine field-filling strategy
                AtomicBoolean needToFill = new AtomicBoolean(true);

                schemaMap.forEach((sk, sv) -> {
                    if (sk.equals(EXTENDS)) {
                        Map<String, Object> extendsMap = castObjectToMap(sv);
                        String fromClass = getStringValueIfExistOrElseNull(FROM_CLASS, extendsMap);
                        String fromPackage = getStringValueIfExistOrElseNull(FROM_PACKAGE, extendsMap);
                        System.out.println("SHOULD EXTENDS FROM: " + fromClass);
                        schema.setExtendsFrom(fromClass);
                        schema.getImportSet().add(fromPackage + "." + fromClass + ";");
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

                // Infer missing 'type: object' when properties/enum/$ref present (AsyncAPI v3 compatibility)
                String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
                if (schemaType == null) {
                    if (schemaMap.containsKey(PROPERTIES) ||
                        schemaMap.containsKey(ENUMERATION) ||
                        schemaMap.containsKey(REFERENCE) ||
                        POLYMORPHS.stream().anyMatch(schemaMap::containsKey)) {
                        schemaType = OBJECT;
                        schemaMap.put(TYPE, OBJECT); // patch in-place
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
                    // Soft skip: no type + no structural keys → ignore silently
                    System.out.println("SKIP INNER SCHEMA (no type + no props/enum/ref): " + schemaName);
                }
            });
        }
        return schemaList;
    }

    /**
     * Extracts field definitions and metadata for a schema.
     * <p>
     * Handles:
     * <ul>
     *   <li>Direct {@code properties} map</li>
     *   <li>Polymorphic schemas (merges fields from all variants)</li>
     *   <li>Top-level enums (delegates to {@link AbstractMapper#fillProperties})</li>
     * </ul>
     *
     * @param schemaName     schema name (for diagnostics)
     * @param currentSchema  full schema map
     * @param schemas        global schemas map (for {@code $ref} resolution)
     * @param properties     {@code properties} map (may be empty)
     * @param processContext generation context
     * @param innerSchemas   accumulator for discovered inner schemas
     * @return filled {@link FillParameters}
     */
    public FillParameters getSchemaVariableProperties(String schemaName,
                                                      Map<String, Object> currentSchema,
                                                      Map<String, Object> schemas,
                                                      Map<String, Object> properties,
                                                      ProcessContext processContext,
                                                      Map<String, Object> innerSchemas) {
        List<VariableProperties> variableProperties = new LinkedList<>();
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
        if (POLYMORPHS.stream().anyMatch(p -> currentSchema.containsKey(p))) {
            System.out.println("POLYMORPH: " + schemaName);
            System.out.println("POLYMORPH: " + currentSchema);
            // Recursively collect all referenced schema names (including nested polymorphism)
            List<String> polymorphSchemasNames = getPolymorphSchemasNames(currentSchema, schemas);
            System.out.println(polymorphSchemasNames);

            // Merge all properties from referenced schemas
            Map<String, Object> mergedProperties = mergeProperties(polymorphSchemasNames, currentSchema, schemas);

            // Add each merged property (avoid duplicates)
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
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, currentSchema) != null) {
            // Top-level enum: fill as enum field (no properties)
            VariableProperties vp = new VariableProperties();
            vp.setValid(false);
            vp.setEnum(true);
            fillProperties(schemaName, vp, currentSchema, schemas, schemaName, currentSchema, processContext, processContext.getHelper().getInnerSchemas());
            variableProperties.add(vp);
        }
        return new FillParameters(variableProperties);
    }

    /**
     * Recursively collects all schema names referenced via {@code oneOf}/{@code allOf}/{@code anyOf}.
     * <p>
     * Supports nested polymorphism (e.g., {@code A → oneOf: B, C; B → allOf: D, E} → [D, E, B, C]).
     *
     * @param currentSchema schema with polymorphic keys
     * @param schemas       global schema map
     * @return list of fully resolved schema names
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
                    // Recursively resolve nested polymorphic schemas
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
     * Merges properties from multiple schemas (used for polymorphism).
     * <p>
     * Collects {@code properties} from each referenced schema and merges them into one flat map.
     * Duplicates are resolved by keeping the first occurrence.
     *
     * @param polymorphSchemasNames referenced schema names
     * @param currentSchema         current (polymorphic) schema
     * @param schemas               schema registry
     * @return merged properties map
     */
    private Map<String, Object> mergeProperties(List<String> polymorphSchemasNames, Map<String, Object> currentSchema, Map<String, Object> schemas) {
        return polymorphSchemasNames.stream()
                .flatMap(name -> {
                    Map<String, Object> schema = castObjectToMap(schemas.get(name));
                    if (schema.containsKey(ALL_OF) || schema.containsKey(ANY_OF) || schema.containsKey(ONE_OF)) {
                        // For polymorphic schemas, prefer top-level properties over embedded ones
                        Map<String, Object> propertyMap = schema.entrySet().stream()
                                .filter(en -> en.getKey().equals(PROPERTIES))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing));
                        propertyMap.putAll(castObjectToMap(schemas.get(name)));
                        return propertyMap.entrySet().stream();
                    }
                    return castObjectToMap(schemas.get(name)).entrySet().stream();
                })
                .filter(en -> en.getKey().equals(PROPERTIES))
                .map(pr -> castObjectToMap(pr.getValue()))
                .flatMap(map -> map.entrySet().stream())
                .distinct()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (existing, replacement) -> existing));
    }
}