package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.util.MapperUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Base class for schema and message mapping logic.
 * <p>
 * Responsible for:
 * <ul>
 *   <li>Populating {@link VariableProperties} from YAML property definitions</li>
 *   <li>Resolving {@code $ref}, polymorphism ({@code oneOf}/{@code allOf}), collections, maps, enums</li>
 *   <li>Inferring Java types, validation annotations, and required imports</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class AbstractMapper {

    /**
     * Populates a {@link VariableProperties} instance from a single property definition in a schema.
     * <p>
     * Handles:
     * <ul>
     *   <li>Basic metadata: name, description, example, default</li>
     *   <li>Validation: min/max length/size, pattern, digits, min/max value</li>
     *   <li>Type/format inference ({@code string}, {@code date}, {@code uuid}, etc.)</li>
     *   <li>Polymorphism detection ({@code oneOf}, {@code allOf}, {@code anyOf})</li>
     *   <li>Delegation to {@link #fillVariableProperties} for type resolution</li>
     *   <li>Required annotations/imports for validation</li>
     * </ul>
     *
     * @param schemaName         name of the containing schema (for diagnostics)
     * @param variableProperties target to populate
     * @param currentSchema      full current schema map (used for {@code required} lookup)
     * @param schemas            global schemas map (for {@code $ref} resolution)
     * @param propertyName       raw property name from YAML
     * @param propertiesMap      property definition map (e.g., {@code { type: string, format: email }})
     * @param processContext     current generation context
     * @param innerSchemas       accumulator for generated inner schemas (e.g., enums, inner DTOs)
     */
    public void fillProperties(String schemaName,
                               VariableProperties variableProperties,
                               Map<String, Object> currentSchema,
                               Map<String, Object> schemas,
                               String propertyName,
                               Map<String, Object> propertiesMap,
                               ProcessContext processContext,
                               Map<String, Object> innerSchemas) {
        variableProperties.setSpringBootVersion(processContext.getSpringBootVersion());
        variableProperties.setName(safeFieldName(uncapitalize(propertyName)));
        variableProperties.setDefaultProperty(getStringValueIfExistOrElseNull(DEFAULT, propertiesMap));
        variableProperties.setType(capitalize(getStringValueIfExistOrElseNull(TYPE, propertiesMap)));
        variableProperties.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, propertiesMap));
        variableProperties.setExample(getStringValueIfExistOrElseNull(EXAMPLE, propertiesMap));
        variableProperties.setTitle(getStringValueIfExistOrElseNull(TITLE, propertiesMap));
        variableProperties.setDigits(getStringValueIfExistOrElseNull(DIGITS, propertiesMap));
        variableProperties.setMultipleOf(getStringValueIfExistOrElseNull(MULTIPLE_OF, propertiesMap));
        variableProperties.setMinimum(getStringValueIfExistOrElseNull(MINIMUM, propertiesMap));
        variableProperties.setMaximum(getStringValueIfExistOrElseNull(MAXIMUM, propertiesMap));
        variableProperties.setMinMaxLength(getStringValueIfExistOrElseNull(MIN_LENGTH, propertiesMap), getStringValueIfExistOrElseNull(MAX_LENGTH, propertiesMap));
        variableProperties.setFormat(getStringValueIfExistOrElseNull(FORMAT, propertiesMap));
        variableProperties.setPattern(getStringValueIfExistOrElseNull(PATTERN, propertiesMap));
        variableProperties.setEnumeration(getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap));
        variableProperties.setEnumNames(getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap));
        variableProperties.setPackageOfExisingObject(getStringValueIfExistOrElseNull(PACKAGE, propertiesMap));
        variableProperties.setNameOfExisingObject(getStringValueIfExistOrElseNull(NAME, propertiesMap));
        variableProperties.setOriginalEnumName(propertyName);
        variableProperties.setPolymorph(
                !POLYMORPHS.stream()
                        .map(p -> propertiesMap.get(p))
                        .map(MapperUtil::castObjectToListObjects)
                        .flatMap(objects -> objects.stream())
                        .collect(Collectors.toList()).isEmpty());
        fillVariableProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
        fillRequiredAnnotationsAndImports(variableProperties, currentSchema, propertyName);
    }

    /**
     * Resolves the Java type and metadata for a property, based on its definition.
     * <p>
     * Dispatches to specialized handlers for:
     * <ul>
     *   <li>Empty property → {@code Object}</li>
     *   <li>{@code format} override for {@code type: object} (e.g., {@code uuid}, {@code date})</li>
     *   <li>Arrays/collections</li>
     *   <li>{@code $ref} references</li>
     *   <li>Inner objects (with {@code properties})</li>
     *   <li>Enums</li>
     *   <li>Existing classes ({@code format: existing})</li>
     *   <li>Maps ({@code additionalProperties})</li>
     *   <li>Polymorphic types</li>
     * </ul>
     *
     * @param schemaName         schema name (for diagnostics)
     * @param variableProperties target to populate
     * @param currentSchema      current schema map
     * @param schemas            global schemas map
     * @param propertyName       property name
     * @param propertiesMap      property definition
     * @param processContext     generation context
     * @param innerSchemas       inner schema accumulator
     */
    public void fillVariableProperties(String schemaName,
                                       VariableProperties variableProperties,
                                       Map<String, Object> currentSchema,
                                       Map<String, Object> schemas,
                                       String propertyName,
                                       Map<String, Object> propertiesMap,
                                       ProcessContext processContext,
                                       Map<String, Object> innerSchemas) {
        String commonPackage = processContext.getCommonPackage();
        // 1. Empty property → Object
        if (propertiesMap.isEmpty()) {
            variableProperties.setType(OBJECT_TYPE);
            variableProperties.setValid(false);
            return;
        }
        String type = getStringValueIfExistOrElseNull(TYPE, propertiesMap);
        String format = getStringValueIfExistOrElseNull(FORMAT, propertiesMap);
        // 2. type: object + format → scalar (e.g. uuid/date) — handle before fallback
        if (OBJECT_TYPE.equalsIgnoreCase(type) && format != null &&
            JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(format)) {
            variableProperties.setFormat(format);
            return;
        }
        // fallback: Object if type absent or 'object'
        variableProperties.setType(capitalize(type != null ? type : OBJECT_TYPE));
        // Dispatch by structural type
        if (ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillArrayProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
        }
        // $ref
        else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillReferenceProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
        }
        // object with properties
        else if (OBJECT_TYPE.equals(variableProperties.getType()) &&
                 getStringValueIfExistOrElseNull(PROPERTIES, propertiesMap) != null) {
            fillObjectProperties(schemaName, variableProperties, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
        }
        // enum
        else if ((OBJECT_TYPE.equals(variableProperties.getType()) || STRING.equals(variableProperties.getType())) &&
                 getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            fillEnumProperties(variableProperties, propertyName, propertiesMap, commonPackage, innerSchemas);
        }
        // format: existing
        else if (OBJECT_TYPE.equals(variableProperties.getType()) &&
                 variableProperties.getPackageOfExisingObject() != null &&
                 variableProperties.getNameOfExisingObject() != null) {
            fillExistingObjectProperties(variableProperties);
        }
        // map (additionalProperties)
        else if (getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null) {
            fillMapProperties(variableProperties, currentSchema, schemas, propertiesMap, commonPackage);
        }
        // polymorphism
        else if (variableProperties.isPolymorph()) {
            System.out.println("FOUND POLYMORPHISM INSIDE SCHEMA! Schema: " + variableProperties.getName());
            List<Object> polymorphList = POLYMORPHS.stream()
                    .map(p -> propertiesMap.get(p))
                    .map(MapperUtil::castObjectToListObjects)
                    .flatMap(objects -> objects.stream())
                    .collect(Collectors.toList());
            // Merge properties from all referenced schemas
            Map<String, Object> mergedProperties = polymorphList.stream()
                    .flatMap(ref -> {
                        String refStr = ref.toString();
                        if (ref instanceof Map) {
                            Map<?, ?> mapRef = (Map<?, ?>) ref;
                            Object r = mapRef.get("$ref");
                            if (r != null) refStr = r.toString();
                        }
                        return castObjectToMap(schemas.get(refReplace(refStr))).entrySet().stream();
                    })
                    .filter(en -> en.getKey().equals(PROPERTIES))
                    .map(pr -> castObjectToMap(pr.getValue()))
                    .flatMap(map -> map.entrySet().stream())
                    .distinct()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> existing
                    ));
            // Build class name: propName + Ref1 + Ref2 + ...
            String className = capitalize(propertyName);
            for (Object item : polymorphList) {
                String refName;
                if (item instanceof Map) {
                    Map<?, ?> m = (Map<?, ?>) item;
                    Object refObj = m.get("$ref");
                    refName = refObj != null ? refReplace(refObj.toString()) : "Unknown";
                } else {
                    refName = refReplace(item.toString());
                }
                className += refName;
            }
            variableProperties.setType(className);
            variableProperties.addRequiredImports(prepareImport(commonPackage, className));
            Map<String, Object> preparedMergedPolymorphSchema = Map.of(
                    className,
                    Map.of(TYPE, OBJECT, PROPERTIES, mergedProperties)
            );
            innerSchemas.putAll(preparedMergedPolymorphSchema);
        }
        // fallback: bare object → Object
        else {
            if (OBJECT_TYPE.equals(variableProperties.getType())) {
                variableProperties.setType(OBJECT_TYPE);
                variableProperties.setValid(false);
            }
            // Ensure format applied even for scalars (e.g., type: string, format: uuid)
            variableProperties.setFormat(format);
        }
        // Add imports for collection/map types
        String finalType = variableProperties.getType();
        if (finalType != null) {
            if (finalType.startsWith("List<")) {
                variableProperties.addRequiredImports(LIST_IMPORT);
            } else if (finalType.startsWith("Set<")) {
                variableProperties.addRequiredImports(SET_IMPORT);
            } else if (finalType.startsWith("Map<")) {
                variableProperties.addRequiredImports(MAP_IMPORT);
            }
        }
    }

    /**
     * Prepares a full import declaration for a class in the common package.
     *
     * @param commonPackage base common package (e.g., {@code "com.example.common;"})
     * @param className     simple class name
     * @return full import string (e.g., {@code "com.example.common.MyClass;"})
     */
    private String prepareImport(String commonPackage, String className) {
        return commonPackage.replace(";", "." + className + ";");
    }

    /**
     * Handles {@code additionalProperties} → Java {@code Map<K,V>}.
     * <p>
     * Supports:
     * <ul>
     *   <li>Scalar value types ({@code string}, {@code integer}, etc.)</li>
     *   <li>Referenced types ({@code $ref})</li>
     *   <li>Existing classes ({@code format: existing})</li>
     *   <li>Nested collections/maps</li>
     * </ul>
     *
     * @param variableProperties target field
     * @param currentSchema      current schema map
     * @param schemas            global schemas
     * @param propertiesMap      property map containing {@code additionalProperties}
     * @param commonPackage      target common package
     */
    private void fillMapProperties(VariableProperties variableProperties,
                                   Map<String, Object> currentSchema,
                                   Map<String, Object> schemas,
                                   Map<String, Object> propertiesMap,
                                   String commonPackage) {
        System.out.println();
        System.out.println("ADDITIONAL PROPERTIES");
        Map<String, Object> additionalPropertiesMap = castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES));
        String type = getStringValueIfExistOrElseNull(TYPE, additionalPropertiesMap);
        String format = variableProperties.getFormat();
        String referencedObject = getStringValueIfExistOrElseNull(REFERENCE, additionalPropertiesMap);
        variableProperties.addRequiredImports(MAP_IMPORT);
        variableProperties.setRealisation(getStringValueIfExistOrElseNull(REALIZATION, propertiesMap));
        variableProperties.setValid(false);
        // Scalar value type
        if (type != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(type)) {
            System.out.println("CORRECT TYPE!");
            if (format != null) {
                variableProperties.setType(format(MAP_CUSTOM_TYPE,
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(format),
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(type)));
            } else {
                variableProperties.setType(format(MAP_TYPE, JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(type)));
            }
        }
        // Existing object (format: existing)
        else if (OBJECT.equals(type) && referencedObject == null) {
            if (getStringValueIfExistOrElseNull(PACKAGE, additionalPropertiesMap) != null) {
                if (format != null) {
                    variableProperties.setType(format(MAP_CUSTOM_TYPE,
                            JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                            capitalize(getStringValueIfExistOrElseNull(NAME, additionalPropertiesMap))));
                    variableProperties.addRequiredImports(getStringValueIfExistOrElseNull(PACKAGE, additionalPropertiesMap)
                            .concat(".")
                            .concat(capitalize(getStringValueIfExistOrElseNull(NAME, additionalPropertiesMap)))
                            .concat(";"));
                } else {
                    variableProperties.setType(format(MAP_TYPE, capitalize(getStringValueIfExistOrElseNull(NAME, additionalPropertiesMap))));
                    variableProperties.addRequiredImports(getStringValueIfExistOrElseNull(PACKAGE, additionalPropertiesMap)
                            .concat(".")
                            .concat(capitalize(getStringValueIfExistOrElseNull(NAME, additionalPropertiesMap)))
                            .concat(";"));
                }
            } else {
                if (format != null) {
                    variableProperties.setType(format(MAP_CUSTOM_TYPE,
                            JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                            OBJECT_TYPE));
                } else {
                    variableProperties.setType(format(MAP_TYPE, OBJECT_TYPE));
                }
            }
        }
        // $ref to schema
        else if (referencedObject != null && (schemas.containsKey(refReplace(referencedObject)) || currentSchema == schemas)) {
            String refObjectName = refReplace(referencedObject);
            System.out.println("FOUND CUSTOM OBJECT! " + refObjectName);
            if (ARRAY.equals(type)) {
                String collectionType = getStringValueIfExistOrElseNull(FORMAT, additionalPropertiesMap);
                if (collectionType != null) {
                    variableProperties.setCollectionType(collectionType);
                }
                String javaType = getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, additionalPropertiesMap);
                if (javaType != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(javaType)) {
                    if (format != null) {
                        variableProperties.setItems(JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(javaType).toString());
                        fillCollectionType(variableProperties);
                        variableProperties.setType(format(MAP_CUSTOM_TYPE,
                                JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                                variableProperties.getType()));
                    } else {
                        variableProperties.setItems(JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(javaType).toString());
                        fillCollectionType(variableProperties);
                        variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
                    }
                } else {
                    if (format != null) {
                        variableProperties.setItems(refObjectName);
                        fillCollectionType(variableProperties);
                        variableProperties.setType(format(MAP_CUSTOM_TYPE,
                                JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                                variableProperties.getType()));
                        variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
                    } else {
                        variableProperties.setItems(refObjectName);
                        fillCollectionType(variableProperties);
                        variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
                        variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
                    }
                }
            } else {
                if (format != null) {
                    variableProperties.setType(format(MAP_CUSTOM_TYPE,
                            JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                            refObjectName));
                    variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
                } else {
                    variableProperties.setType(format(MAP_TYPE, refObjectName));
                    variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
                }
            }
        }
        // Array of scalars
        else if (ARRAY.equals(type) && getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, additionalPropertiesMap) != null) {
            String collectionType = getStringValueIfExistOrElseNull(FORMAT, additionalPropertiesMap);
            if (collectionType != null) {
                variableProperties.setCollectionType(collectionType);
            }
            variableProperties.setItems(JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, additionalPropertiesMap)).toString());
            fillCollectionType(variableProperties);
            if (format != null) {
                variableProperties.setType(format(MAP_CUSTOM_TYPE,
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                        variableProperties.getType()));
            } else {
                variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
            }
        }
        // Array of existing objects
        else if (ARRAY.equals(type) && getStringValueIfExistOrElseNull(PACKAGE, additionalPropertiesMap) != null) {
            String collectionType = getStringValueIfExistOrElseNull(FORMAT, additionalPropertiesMap);
            if (collectionType != null) {
                variableProperties.setCollectionType(collectionType);
            }
            fillCollectionWithExistingObject(variableProperties, additionalPropertiesMap);
            if (format != null) {
                variableProperties.setType(format(MAP_CUSTOM_TYPE,
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(variableProperties.getFormat()),
                        variableProperties.getType()));
            } else {
                variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
            }
        }
        System.out.println();
    }

    /**
     * Handles {@code format: existing} → external Java class.
     *
     * @param variableProperties target field
     */
    private static void fillExistingObjectProperties(VariableProperties variableProperties) {
        variableProperties.setType(capitalize(variableProperties.getNameOfExisingObject()));
        variableProperties.addRequiredImports(variableProperties.getPackageOfExisingObject()
                .concat(".")
                .concat(capitalize(variableProperties.getNameOfExisingObject()))
                .concat(";"));
    }

    /**
     * Handles enum definitions (inline or referenced).
     *
     * @param variableProperties target field
     * @param propertyName       enum property name
     * @param propertiesMap      property definition
     * @param commonPackage      common package for enum import
     * @param innerSchemas       accumulator for generated enum schema
     */
    private void fillEnumProperties(VariableProperties variableProperties,
                                    String propertyName,
                                    Map<String, Object> propertiesMap,
                                    String commonPackage,
                                    Map<String, Object> innerSchemas) {
        System.out.println("ENUMERATION FOUND: " + propertyName);
        fillEnumSchema(propertyName, propertiesMap, innerSchemas);
        variableProperties.setType(capitalize(propertyName));
        variableProperties.setValid(false);
        variableProperties.setEnumNames(null);
        variableProperties.setEnumeration(null);
        variableProperties.setEnum(true);
        variableProperties.addRequiredImports(prepareImport(commonPackage, capitalize(propertyName)));
    }

    /**
     * Handles inner object schemas (with {@code properties}).
     *
     * @param schemaName         parent schema name
     * @param variableProperties target field
     * @param schemas            global schemas
     * @param propertyName       property name
     * @param propertiesMap      inner schema definition
     * @param commonPackage      common package
     * @param innerSchemas       accumulator
     */
    private void fillObjectProperties(String schemaName,
                                      VariableProperties variableProperties,
                                      Map<String, Object> schemas,
                                      String propertyName,
                                      Map<String, Object> propertiesMap,
                                      String commonPackage,
                                      Map<String, Object> innerSchemas) {
        String propName;
        if (schemas.containsKey(propertyName) || schemas.containsKey(capitalize(propertyName)) ||
            innerSchemas.containsKey(propertyName) || innerSchemas.containsKey(capitalize(propertyName))) {
            propName = capitalize(schemaName).concat(capitalize(propertyName));
        } else {
            propName = propertyName;
        }
        fillInnerSchema(variableProperties, propName, propertiesMap, commonPackage, innerSchemas);
    }

    /**
     * Resolves {@code $ref} to external or internal schema.
     *
     * @param schemaName         current schema name
     * @param variableProperties target field
     * @param currentSchema      current schema map
     * @param schemas            global schemas
     * @param propertyName       property name
     * @param propertiesMap      property definition
     * @param processContext     generation context
     * @param innerSchemas       accumulator
     */
    private void fillReferenceProperties(String schemaName,
                                         VariableProperties variableProperties,
                                         Map<String, Object> currentSchema,
                                         Map<String, Object> schemas,
                                         String propertyName,
                                         Map<String, Object> propertiesMap,
                                         ProcessContext processContext,
                                         Map<String, Object> innerSchemas) {
        String referenceObject = propertiesMap.get(REFERENCE).toString();
        Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
        String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
        if (objectType != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(objectType)) {
            // Recursive fill for scalar $ref (e.g., $ref: ./schemas.yaml#/components/schemas/MyDate)
            System.out.println();
            System.out.println("Start Recursive fillProperties " + propertyName);
            System.out.println();
            fillProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, stringObjectMap, processContext, innerSchemas);
        }
        if (variableProperties.getType() == null || OBJECT_TYPE.equals(variableProperties.getType())) {
            String refReplace = refReplace(referenceObject);
            if (getStringValueIfExistOrElseNull(refReplace, schemas) != null
                && castObjectToMap(schemas.get(refReplace)).entrySet().stream()
                        .anyMatch(p -> POLYMORPHS.contains(p.getKey()))) {
                System.out.println("SKIP INHERITANCE POLYMORPH INSIDE SCHEMA! " + refReplace);
                variableProperties.setType(refReplace);
                variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), refReplace));
            } else {
                variableProperties.setType(refReplace);
                String possibleReferencedEnum = getStringValueIfExistOrElseNull(refReplace, schemas);
                if (possibleReferencedEnum != null) {
                    Map<String, Object> possibleEnumSchema = castObjectToMap(schemas.get(refReplace));
                    if (!possibleEnumSchema.isEmpty() && getStringValueIfExistOrElseNull(ENUMERATION, possibleEnumSchema) != null) {
                        variableProperties.setValid(false);
                    }
                }
                // Support external package from $ref (injected via preprocess)
                String externalPackage = getStringValueIfExistOrElseNull("package", propertiesMap);
                if (externalPackage != null) {
                    String fullImport = externalPackage + "." + refReplace + ";";
                    variableProperties.addRequiredImports(fullImport);
                } else if (Character.isUpperCase(refReplace.charAt(0))) {
                    variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), refReplace));
                }
            }
        }
    }

    /**
     * Handles array/collection types.
     *
     * @param schemaName         schema name
     * @param variableProperties target field
     * @param currentSchema      current schema
     * @param schemas            global schemas
     * @param propertyName       property name
     * @param propertiesMap      property definition
     * @param processContext     context
     * @param innerSchemas       accumulator
     */
    private void fillArrayProperties(String schemaName,
                                     VariableProperties variableProperties,
                                     Map<String, Object> currentSchema,
                                     Map<String, Object> schemas,
                                     String propertyName,
                                     Map<String, Object> propertiesMap,
                                     ProcessContext processContext,
                                     Map<String, Object> innerSchemas) {
        Map<String, Object> items = castObjectToMap(propertiesMap.get(ITEMS));
        variableProperties.setRealisation(getStringValueIfExistOrElseNull(REALIZATION, items));
        String refValue = getStringValueIfExistOrElseNull(REFERENCE, items);
        String collectionFormat = getStringValueIfExistOrElseNull(FORMAT, propertiesMap);
        boolean javaType = false;
        // Normalize empty items: {} → { type: object }
        if (items.isEmpty()) {
            variableProperties.setItems(OBJECT_TYPE);
            variableProperties.setValid(false);
            fillCollectionType(variableProperties);
            return;
        }
        if (collectionFormat != null) {
            variableProperties.setCollectionType(collectionFormat);
        }
        if (refValue != null) {
            String tryToFound = refValue.replaceAll(".+/", "");
            Map<String, Object> schemaRef = castObjectToMap(schemas.get(tryToFound));
            if (getStringValueIfExistOrElseNull(PROPERTIES, schemaRef) == null) {
                String typeOfRefObj = getStringValueIfExistOrElseNull(TYPE, schemaRef);
                String formatOfRefObj = getStringValueIfExistOrElseNull(FORMAT, schemaRef);
                String descOfRefObj = getStringValueIfExistOrElseNull(DESCRIPTION, schemaRef);
                if (descOfRefObj != null) {
                    variableProperties.setDescription(descOfRefObj);
                }
                if (formatOfRefObj != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(typeOfRefObj)) {
                    variableProperties.setItems(formatOfRefObj);
                    variableProperties.setFormat(formatOfRefObj);
                } else {
                    variableProperties.setItems(refReplace(refValue));
                }
            } else {
                variableProperties.setItems(refReplace(refValue));
            }
            fillCollectionType(variableProperties);
        } else {
            if (getStringValueIfExistOrElseNull(FORMAT, items) != null || getStringValueIfExistOrElseNull(TYPE, items) != null) {
                if (getStringValueIfExistOrElseNull(FORMAT, items) != null) {
                    if (getStringValueIfExistOrElseNull(FORMAT, items).equals(EXISTING)) {
                        fillCollectionWithExistingObject(variableProperties, items);
                    } else {
                        // Scalar format (e.g., items: { format: date })
                        variableProperties.setItems(items.get(FORMAT).toString());
                        variableProperties.setFormat(items.get(FORMAT).toString());
                    }
                } else {
                    if (getStringValueIfExistOrElseNull(PROPERTIES, items) != null) {
                        fillObjectProperties(schemaName, variableProperties, schemas, propertyName, items, processContext.getCommonPackage(), innerSchemas);
                        variableProperties.setType(format(LIST_TYPE, variableProperties.getType()));
                    } else {
                        if (getStringValueIfExistOrElseNull(PACKAGE, items) != null) {
                            fillCollectionWithExistingObject(variableProperties, items);
                        } else {
                            variableProperties.setItems(items.get(TYPE).toString());
                            variableProperties.setFormat(items.get(TYPE).toString());
                        }
                    }
                }
            } else {
                fillProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, items, processContext, innerSchemas);
                variableProperties.setItems(capitalize(propertyName));
                fillCollectionType(variableProperties);
            }
        }
        if (variableProperties.getItems() != null &&
            !JAVA_DEFAULT_TYPES.contains(variableProperties.getItems()) &&
            !OBJECT_TYPE.equals(variableProperties.getItems()) && !javaType &&
            getStringValueIfExistOrElseNull(PACKAGE, items) == null) {
            // External package support for collection items
            String externalPackage = getStringValueIfExistOrElseNull("package", items);
            if (externalPackage != null) {
                variableProperties.addRequiredImports(externalPackage + "." + variableProperties.getItems() + ";");
            } else {
                variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), variableProperties.getItems()));
            }
        }
    }

    /**
     * Handles collections of {@code format: existing} objects.
     *
     * @param variableProperties target field
     * @param items              items definition map
     */
    private static void fillCollectionWithExistingObject(VariableProperties variableProperties, Map<String, Object> items) {
        variableProperties.setItems(getStringValueIfExistOrElseNull(NAME, items));
        variableProperties.setPackageOfExisingObject(getStringValueIfExistOrElseNull(PACKAGE, items));
        variableProperties.setNameOfExisingObject(getStringValueIfExistOrElseNull(NAME, items));
        variableProperties.addRequiredImports(variableProperties.getPackageOfExisingObject()
                .concat(".")
                .concat(capitalize(variableProperties.getNameOfExisingObject()))
                .concat(";"));
        fillCollectionType(variableProperties);
    }

    /**
     * Registers an inner schema (e.g., for nested DTO).
     *
     * @param variableProperties target field
     * @param propertyName       inner schema name
     * @param propertiesMap      inner schema definition
     * @param commonPackage      common package
     * @param innerSchemas       accumulator
     */
    private void fillInnerSchema(VariableProperties variableProperties,
                                 String propertyName,
                                 Map<String, Object> propertiesMap,
                                 String commonPackage,
                                 Map<String, Object> innerSchemas) {
        System.out.println("FOUND INNER SCHEMA!!! " + propertyName);
        variableProperties.setType(capitalize(propertyName));
        variableProperties.addRequiredImports(prepareImport(commonPackage, capitalize(propertyName)));
        // Create a defensive copy
        Map<String, Object> innerSchema = new LinkedHashMap<>(propertiesMap);
        // Ensure "type: object" is present — critical for AsyncAPI v3 where it’s optional
        if (!innerSchema.containsKey(TYPE)) {
            innerSchema.put(TYPE, OBJECT);
        }
        // Ensure "properties" key exists if we’re dealing with a pure property map (e.g., {id: {type: string}})
        if (!innerSchema.containsKey(PROPERTIES) && !innerSchema.containsKey(ENUMERATION) && !innerSchema.containsKey(REFERENCE)) {
            // Heuristic: assume top-level keys are properties if no special keys present
            Map<String, Object> actualProps = new LinkedHashMap<>();
            Iterator<Map.Entry<String, Object>> it = innerSchema.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                // Skip known metadata keys
                if (!(key.equals(TYPE) || key.equals(FORMAT) || key.equals(DESCRIPTION) ||
                      key.equals(EXAMPLE) || key.equals(DEFAULT) || key.equals(PATTERN) ||
                      key.equals(MIN_LENGTH) || key.equals(MAX_LENGTH) ||
                      key.equals(MINIMUM) || key.equals(MAXIMUM) ||
                      key.equals(DIGITS) ||
                      key.equals(REALIZATION) || key.equals(NAME) || key.equals(PACKAGE))) {
                    actualProps.put(key, entry.getValue());
                    it.remove(); // move out of root into properties
                }
            }
            if (!actualProps.isEmpty()) {
                innerSchema.put(PROPERTIES, actualProps);
            }
        }
        innerSchemas.put(propertyName, innerSchema);
    }

    /**
     * Extracts message definitions from AsyncAPI 2.0 {@code channels}/{@code publish}/{@code subscribe}.
     *
     * @param allContent     full spec document
     * @param messagesMap    output map to populate
     * @param excludeSchemas schemas to preserve (for polymorphic cases)
     * @param mapToMessage   channel message map
     * @param channelName    channel name
     * @param channelType    {@code "publish"} or {@code "subscribe"}
     */
    public static void fillMessageFromChannel(Map<String, Object> allContent,
                                              Map<String, Object> messagesMap,
                                              Set<String> excludeSchemas,
                                              Map<String, Object> mapToMessage,
                                              String channelName,
                                              String channelType) {
        mapToMessage.entrySet().stream()
                .flatMap(e -> castObjectToMap(e.getValue()).entrySet().stream())
                .filter(e -> e.getKey().equals("message"))
                .flatMap(e -> castObjectToMap(e.getValue()).entrySet().stream())
                .filter(e -> e.getKey().equals(PAYLOAD))
                .forEach(e -> {
                    Map<String, Object> messageValues = castObjectToMap(e.getValue());
                    if (messageValues.containsKey(REFERENCE)) {
                        String messageName = refReplace(messageValues.get(REFERENCE).toString());
                        messagesMap.put(messageName, Map.of(PAYLOAD, messageValues));
                    }
                    if (messageValues.containsKey(ONE_OF) || messageValues.containsKey(ALL_OF) || messageValues.containsKey(ANY_OF)) {
                        String messageName = capitalize(channelName)
                                .concat(capitalize(channelType))
                                .concat("Message");
                        System.out.println(messageName);
                        List<Object> polymorphList = POLYMORPHS.stream()
                                .map(p -> messageValues.get(p))
                                .map(MapperUtil::castObjectToListObjects)
                                .flatMap(objects -> objects.stream())
                                .collect(Collectors.toList());
                        if (!polymorphList.stream()
                                .flatMap(refPair -> castObjectToMap(refPair).entrySet().stream())
                                .map(en -> refReplace(en.getValue().toString()))
                                .map(ref -> getSchemaByName(allContent, ref))
                                .flatMap(map -> map.entrySet().stream())
                                .filter(en -> POLYMORPHS.contains(en.getKey()))
                                .collect(Collectors.toList()).isEmpty()) {
                            System.out.println("FOUND POLYMORPHISM INSIDE SCHEMA!");
                            List<Object> filteredReferences = polymorphList.stream()
                                    .flatMap(refPair -> castObjectToMap(refPair).entrySet().stream())
                                    .map(en -> {
                                        String schemaName = refReplace(en.getValue().toString());
                                        excludeSchemas.add(schemaName);
                                        return schemaName;
                                    })
                                    .map(ref -> getSchemaByName(allContent, ref))
                                    .map(MapperUtil::castObjectToMap)
                                    .flatMap(refObj -> refObj.entrySet().stream()
                                            .filter(en -> POLYMORPHS.contains(en.getKey()))
                                            .map(all -> castObjectToListObjects(all.getValue()).stream()
                                                    .filter(o -> o.toString().startsWith("{" + REFERENCE))
                                                    .collect(Collectors.toList())
                                            ))
                                    .collect(Collectors.toList());
                            // Create propertiesMap from all referenced objects properties
                            Map<String, Object> propertiesMap = polymorphList.stream()
                                    .flatMap(refPair -> castObjectToMap(refPair).entrySet().stream())
                                    .map(en -> refReplace(en.getValue().toString()))
                                    .map(ref -> getSchemaByName(allContent, ref))
                                    .map(MapperUtil::castObjectToMap)
                                    .flatMap(map -> map.entrySet().stream())
                                    .filter(en -> POLYMORPHS.contains(en.getKey()))
                                    .flatMap(all -> castObjectToListObjects(all.getValue()).stream())
                                    .map(all -> castObjectToMap(all))
                                    .flatMap(map -> map.entrySet().stream())
                                    .filter(en -> en.getKey().equals(PROPERTIES))
                                    .map(pr -> castObjectToMap(pr.getValue()))
                                    .flatMap(map -> map.entrySet().stream())
                                    .distinct()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (existing, replacement) -> existing
                                    ));
                            // Put in the properties all from referenced schemas
                            filteredReferences.stream()
                                    .flatMap(en -> castObjectToListObjects(en).stream())
                                    .distinct()
                                    .map(MapperUtil::castObjectToMap)
                                    .flatMap(ey -> ey.entrySet().stream())
                                    .map(en -> refReplace(en.getValue().toString()))
                                    .map(ref -> getSchemaByName(allContent, ref))
                                    .map(MapperUtil::castObjectToMap)
                                    .map(o -> castObjectToMap(o).get(PROPERTIES))
                                    .map(MapperUtil::castObjectToMap)
                                    .flatMap(ey -> ey.entrySet().stream())
                                    .forEach(el -> propertiesMap.put(el.getKey(), el.getValue()));
                            // Fill message map: MessageName → { payload: { type: object, properties: {...} } }
                            messagesMap.put(messageName, Map.of(PAYLOAD, Map.of(TYPE, OBJECT, PROPERTIES, propertiesMap)));
                        } else {
                            // Prepare map with merged properties
                            Map<String, Object> schemaMap = polymorphList.stream()
                                    .flatMap(refPair -> castObjectToMap(refPair).entrySet().stream())
                                    .map(en -> {
                                        String schemaName = refReplace(en.getValue().toString());
                                        excludeSchemas.add(schemaName);
                                        return schemaName;
                                    })
                                    .map(ref -> getSchemaByName(allContent, ref))
                                    .map(MapperUtil::castObjectToMap)
                                    .flatMap(map -> map.entrySet().stream())
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (existing, replacement) -> existing
                                    ));
                            // Fill message map: MessageName → { payload: { ... } }
                            messagesMap.put(messageName, Map.of(PAYLOAD, schemaMap));
                        }
                    }
                });
    }

    /**
     * Sets collection type (List/Set) based on {@code collectionType} field.
     *
     * @param variableProperties target field
     */
    private static void fillCollectionType(VariableProperties variableProperties) {
        switch (variableProperties.getCollectionType()) {
            case "list":
                variableProperties.setType(format(refReplace(LIST_TYPE), variableProperties.getItems()));
                break;
            case "set":
                variableProperties.setType(format(refReplace(SET_TYPE), variableProperties.getItems()));
        }
    }

    /**
     * Generates inner schema for an enum (with or without descriptions).
     *
     * @param propertyName  enum name
     * @param propertiesMap property definition
     * @param innerSchemas  accumulator
     */
    private static void fillEnumSchema(String propertyName,
                                       Map<String, Object> propertiesMap,
                                       Map<String, Object> innerSchemas) {
        if (getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap) != null) {
            Map<String, Object> enumerationMap = castObjectToMap(propertiesMap.get(X_ENUM_NAMES));
            Map<String, Object> enums = new LinkedHashMap<>();
            Map<String, Object> enumWithDescription = fillByEnumWithDescription(enumerationMap);
            innerSchemas.put(propertyName, enumWithDescription);
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            List<String> enums = castObjectToList(propertiesMap.get(ENUMERATION));
            Map<String, Object> enumsMap = fillByEnum(enums);
            innerSchemas.put(propertyName, enumsMap);
        }
    }

    /**
     * Builds enum schema with human-readable descriptions.
     *
     * @param enums raw x-enumNames map
     * @return prepared inner schema map
     */
    private static Map<String, Object> fillByEnumWithDescription(Map<String, Object> enums) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> vp = new LinkedHashMap<>();
        Map<String, Object> type = new LinkedHashMap<>();
        Map<String, Object> properties = new LinkedHashMap<>();
        type.put(TYPE, OBJECT);
        enums.forEach((enumName, enumDescription) -> {
            Map<String, Object> prop = new LinkedHashMap<>();
            prop.put(TYPE, format(ENUM_TYPE, enumName, enumDescription));
            prop.put(X_ENUM_NAMES, enumDescription);
            prop.put(ENUMERATION, enumName);
            vp.put(enumName, prop);
        });
        properties.put(PROPERTIES, vp);
        result.putAll(type);
        result.putAll(properties);
        return result;
    }

    /**
     * Builds simple enum schema (no descriptions).
     *
     * @param enums enum constant list
     * @return prepared inner schema map
     */
    private static Map<String, Object> fillByEnum(List<String> enums) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> vp = new LinkedHashMap<>();
        Map<String, Object> type = new LinkedHashMap<>();
        Map<String, Object> properties = new LinkedHashMap<>();
        type.put(TYPE, OBJECT);
        enums.forEach(enumName -> {
            Map<String, Object> prop = new LinkedHashMap<>();
            prop.put(TYPE, enumName);
            prop.put(ENUMERATION, enumName);
            vp.put(enumName, prop);
        });
        properties.put(PROPERTIES, vp);
        result.putAll(type);
        result.putAll(properties);
        return result;
    }

    /**
     * Adds validation annotations and imports based on {@code required} list and field type.
     * <p>
     * Supports:
     * <ul>
     *   <li>Basic: {@code @NotNull}, {@code @NotBlank}, {@code @NotEmpty}</li>
     *   <li>Validation groups ({@code groups = {...}})</li>
     *   <li>{@code jakarta} vs {@code javax} packages (via Spring Boot version)</li>
     * </ul>
     *
     * @param variableProperties field metadata
     * @param currentSchema      schema containing {@code required} list
     * @param propertyName       field name
     */
    public static void fillRequiredAnnotationsAndImports(VariableProperties variableProperties,
                                                         Map<String, Object> currentSchema,
                                                         String propertyName) {
        Set<String> annotationSet = new HashSet<>();
        Set<String> importSet = new HashSet<>();
        Set<String> requiredAttributes = getSetValueIfExistsOrElseEmptySet(REQUIRED, currentSchema);
        Set<String> validationGroups = getSetValueIfExistsOrElseEmptySet(VALIDATION_GROUPS, currentSchema);
        Set<String> validationGroupsImports = getSetValueIfExistsOrElseEmptySet(VALIDATION_GROUPS_IMPORTS, currentSchema);
        Set<String> validationFields = getSetValueIfExistsOrElseEmptySet(VALIDATE_BY_GROUPS, currentSchema);
        String groups = null;
        if (!validationGroups.isEmpty()) {
            if (validationGroupsImports.isEmpty() || validationFields.isEmpty()) {
                throw new SchemaFillException("Please check attributes: validationGroupsImports and validateByGroups. It must not be null, when validationGroups are filled!");
            }
            ArrayList<String> vg = new ArrayList<>(validationGroups);
            StringBuilder stringBuilder = new StringBuilder("(" + GROUPS + "{");
            for (int i = 0; i < vg.size(); i++) {
                if (i == 0) {
                    stringBuilder.append(vg.get(i));
                } else {
                    stringBuilder.append(", ").append(vg.get(i));
                }
            }
            groups = stringBuilder.append("})").toString();
        }
        String finalGroups = groups;
        requiredAttributes.forEach(requiredAttribute -> {
            if (requiredAttribute.equals(propertyName)) {
                if (variableProperties.getItems() != null) {
                    if (variableProperties.getSpringBootVersion() != null && variableProperties.getSpringBootVersion().startsWith("3")) {
                        importSet.add(JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION));
                    } else {
                        importSet.add(JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION));
                    }
                    if (validationFields.contains(propertyName) && finalGroups != null) {
                        String annotationWithGroups = NOT_EMPTY_ANNOTATION + finalGroups;
                        annotationSet.add(annotationWithGroups);
                        importSet.addAll(validationGroupsImports.stream().map(vi -> vi.concat(";")).collect(Collectors.toSet()));
                    } else {
                        annotationSet.add(NOT_EMPTY_ANNOTATION);
                    }
                } else {
                    if (isBlank(variableProperties.getType())) {
                        throw new SchemaFillException("TYPE not found: " + propertyName);
                    }
                    String annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(variableProperties.getType());
                    if (isBlank(annotation)) {
                        annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(OBJECT_TYPE);
                    }
                    // VALIDATION GROUPS FOR JAVA OBJECTS
                    if (validationFields.contains(propertyName) && finalGroups != null) {
                        if (variableProperties.getSpringBootVersion() != null && variableProperties.getSpringBootVersion().startsWith("3")) {
                            importSet.add(JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                        } else {
                            importSet.add(JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                        }
                        annotation = annotation + finalGroups;
                        annotationSet.add(annotation);
                        importSet.addAll(validationGroupsImports.stream().map(vi -> vi.concat(";")).collect(Collectors.toSet()));
                    } else {
                        if (variableProperties.getSpringBootVersion() != null && variableProperties.getSpringBootVersion().startsWith("3")) {
                            importSet.add(JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                        } else {
                            importSet.add(JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                        }
                        annotationSet.add(annotation);
                    }
                }
            }
            validAnnotationCheck(variableProperties, annotationSet, importSet);
        });
        validAnnotationCheck(variableProperties, annotationSet, importSet);
        variableProperties.getRequiredImports().addAll(importSet);
        variableProperties.getAnnotationSet().addAll(annotationSet);
    }

    /**
     * Adds {@code @Valid} annotation for nested objects (excluding enums/collections).
     *
     * @param variableProperties field
     * @param annotationSet      output annotation set
     * @param importSet          output import set
     */
    private static void validAnnotationCheck(VariableProperties variableProperties,
                                             Set<String> annotationSet,
                                             Set<String> importSet) {
        if (!variableProperties.isEnum() &&
            variableProperties.getType() != null &&
            !JAVA_DEFAULT_TYPES.contains(variableProperties.getType()) &&
            variableProperties.isValid()) {
            if (variableProperties.getSpringBootVersion() != null && variableProperties.getSpringBootVersion().startsWith("3")) {
                importSet.add(JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION));
            } else {
                importSet.add(JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION));
            }
            annotationSet.add(VALID_ANNOTATION);
        }
    }
}