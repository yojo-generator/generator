package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.LombokUtils.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class SchemaMapper extends AbstractMapper {

    public SchemaMapper(Helper helper) {
        super(helper);
        this.helper = helper;
    }

    private final Helper helper;

    public List<Schema> mapSchemasToObjects(ProcessContext processContext) {
        helper.setIsMappedFromSchemas(true);
        List<Schema> schemaList = new ArrayList<>();
        Map<String, Object> innerSchemas = new ConcurrentHashMap<>();
        processContext.getSchemasMap().forEach((schemaName, schemaValues) -> {
            LombokProperties finalLombokProperties = LombokProperties.newLombokProperties(processContext.getLombokProperties());
            System.out.println("START MAPPING OF SCHEMA: " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            String format = getStringValueIfExistOrElseNull(FORMAT, schemaMap);
            //Added support of use interfaces like marker, or with some methods and imports
            if (format != null && format.equals(INTERFACE)) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setPackageName(processContext.getCommonPackage());
                schema.setFillParameters(new FillParameters(new ArrayList<>()));
                schema.setInterface(true);
                schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                schema.setMethods(castObjectToMap(schemaMap.get(METHODS)));
                schema.setImports(getSetValueIfExistsOrElseEmptySet(IMPORTS, schemaMap));
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

                // Marker for extending
                // Check, if there are no attributes other than inheritance,
                // then do not fill the DTO with attributes of the parent class
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
                                    innerSchemas
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
        if (!innerSchemas.isEmpty()) {
            innerSchemas.forEach((schemaName, schemaValues) -> {
                LombokProperties finalLombokProperties = LombokProperties.newLombokProperties(processContext.getLombokProperties());
                System.out.println("START MAPPING OF INNER SCHEMA: " + schemaName);
                Map<String, Object> schemaMap = castObjectToMap(schemaValues);
                String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
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
                                    innerSchemas,
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    processContext,
                                    innerSchemas
                            )
                    );
                    schemaList.add(schema);
                } else {
                    throw new SchemaFillException("NOT DEFINED TYPE OF INNER SCHEMA! Schema: " + schemaName);
                }
            });
        }
        return schemaList;
    }

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
                fillProperties(
                        schemaName,
                        vp,
                        currentSchema,
                        schemas,
                        propertyName,
                        castObjectToMap(propertyValue),
                        processContext,
                        innerSchemas);
                variableProperties.add(vp);
            });
        }
        if (POLYMORPHS.stream().anyMatch(p -> currentSchema.containsKey(p))) {
            System.out.println("POLYMORPH: " + schemaName);
            System.out.println("POLYMORPH: " + currentSchema);
            //Found a polymorph schema names here
            List<String> polymorphSchemasNames = getPolymorphSchemasNames(currentSchema, schemas);

            System.out.println(polymorphSchemasNames);

            //Getting merged polymorph properties
            Map<String, Object> mergedProperties = mergeProperties(polymorphSchemasNames, currentSchema, schemas);

            //Prepare VariableProperties
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
                            innerSchemas);
                    variableProperties.add(vp);
                }
            });
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, currentSchema) != null) {
            currentSchema.get(ENUMERATION);
            VariableProperties vp = new VariableProperties();
            vp.setValid(false);
            vp.setEnum(true);
            fillProperties(schemaName, vp, currentSchema, schemas, schemaName, currentSchema, processContext, innerSchemas);
            variableProperties.add(vp);
        }
        return new FillParameters(variableProperties);
    }

    /**
     * Method for getting polymorph schemas names with recursion
     *
     * @param currentSchema
     * @param schemas
     * @return
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
                    //recursievly get polymorph schemas names here
                    if (castObjectToMap(schemas.get(ref)).containsKey(ALL_OF) || castObjectToMap(schemas.get(ref)).containsKey(ANY_OF) || castObjectToMap(schemas.get(ref)).containsKey(ONE_OF)) {
                        List<String> polymorphSchemasNames = getPolymorphSchemasNames(castObjectToMap(schemas.get(ref)), schemas);
                        polymorphSchemasNames.add(ref);
                        return polymorphSchemasNames.stream();
                    }
                    return List.of(ref).stream();
                })
                .collect(Collectors.toList());
    }

    private Map<String, Object> mergeProperties(List<String> polymorphSchemasNames, Map<String, Object> currentSchema, Map<String, Object> schemas) {
        return polymorphSchemasNames.stream()
                .flatMap(name -> {
                    Map<String, Object> schema = castObjectToMap(schemas.get(name));
                    if (schema.containsKey(ALL_OF) || schema.containsKey(ANY_OF) || schema.containsKey(ONE_OF)) {
                        Map<String, Object> propertyMap = schema.entrySet().stream()
                                .filter(en -> en.getKey().equals(PROPERTIES))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing));
                        propertyMap.putAll(castObjectToMap(schemas.get(name)));
                        return propertyMap.entrySet().stream();
                    }
                    return castObjectToMap(schemas.get(name)).entrySet().stream();
                })
                .filter(en -> en.getKey().equals(PROPERTIES))
//                .flatMap(en -> getMap(schemas, currentSchema, en).stream())
                .distinct()
                .map(pr -> castObjectToMap(pr.getValue()))
                .flatMap(map -> map.entrySet().stream())
                .distinct()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (existing, replacement) -> existing));
    }
}