package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.schema.Schema;
import ru.yojo.codegen.exception.SchemaFillException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
@Component
public class SchemaMapper {

    public List<Schema> mapSchemasToObjects(Map<String, Object> schemas,
                                            LombokProperties lombokProperties,
                                            String commonPackage) {
        List<Schema> schemaList = new ArrayList<>();
        Map<String, Object> innerSchemas = new LinkedHashMap<>();
        schemas.forEach((schemaName, schemaValues) -> {
            System.out.println("START MAPPING OF SCHEMA: " + schemaName);
            Map<String, Object> schemaMap = castObjectToMap(schemaValues);
            String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
            if (schemaMap != null && schemaMap.containsKey(LOMBOK)) {
                Map<String, Object> lombokProps = castObjectToMap(schemaMap.get(LOMBOK));
                if (lombokProps.containsKey(ACCESSORS)) {
                    LombokProperties.Accessors acc = new LombokProperties.Accessors(true, false, false);
                    Map<String, Object> accessors = castObjectToMap(lombokProps.get(ACCESSORS));
                    if (accessors.containsKey(FLUENT)) {
                        acc.setFluent(Boolean.valueOf(accessors.get(FLUENT).toString()));
                    }
                    if (accessors.containsKey(CHAIN)) {
                        acc.setChain(Boolean.valueOf(accessors.get(CHAIN).toString()));
                    }
                    lombokProperties.setAccessors(acc);
                }
            }
            if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                Schema schema = new Schema();
                schema.setSchemaName(capitalize(schemaName));
                schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                schema.setLombokProperties(lombokProperties);
                schema.setPackageName(commonPackage);

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
                                    schemas,
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    commonPackage,
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
                System.out.println("START MAPPING OF INNER SCHEMA: " + schemaName);
                Map<String, Object> schemaMap = castObjectToMap(schemaValues);
                String schemaType = getStringValueIfExistOrElseNull(TYPE, schemaMap);
                if (schemaType != null && !JAVA_DEFAULT_TYPES.contains(capitalize(schemaType))) {
                    Schema schema = new Schema();
                    schema.setSchemaName(capitalize(schemaName));
                    schema.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, schemaMap));
                    schema.setLombokProperties(lombokProperties);
                    schema.setPackageName(commonPackage);
                    schema.setFillParameters(
                            getSchemaVariableProperties(
                                    schemaName,
                                    schemaMap,
                                    innerSchemas,
                                    castObjectToMap(schemaMap.get(PROPERTIES)),
                                    commonPackage,
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
                                                      String commonPackage,
                                                      Map<String, Object> innerSchemas) {
        List<VariableProperties> variableProperties = new LinkedList<>();
        if (!properties.isEmpty()) {
            properties.forEach((propertyName, propertyValue) -> {
                VariableProperties vp = new VariableProperties();
                fillProperties(
                        vp,
                        currentSchema,
                        schemas,
                        propertyName,
                        castObjectToMap(propertyValue),
                        commonPackage,
                        innerSchemas);
                variableProperties.add(vp);
            });
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, currentSchema) != null) {
            currentSchema.get(ENUMERATION);
            VariableProperties vp = new VariableProperties();
            vp.setValid(false);
            vp.setEnum(true);
            fillProperties(vp, currentSchema, schemas, schemaName, currentSchema, commonPackage, innerSchemas);
            variableProperties.add(vp);
        }
        return new FillParameters(variableProperties);
    }
}