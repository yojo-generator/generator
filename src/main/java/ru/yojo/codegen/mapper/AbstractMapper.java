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
 * Provides reusable methods to:
 * <ul>
 *   <li>Populate {@link VariableProperties} from raw YAML maps</li>
 *   <li>Handle various field types: primitives, collections, maps, enums, polymorphic schemas, and references</li>
 *   <li>Apply validation annotations and required imports based on {@code required} and format constraints</li>
 *   <li>Resolve inner/anonymous schemas and register them for separate generation</li>
 * </ul>
 *
 * <p>
 * Subclasses {@link ru.yojo.codegen.mapper.SchemaMapper} and {@link ru.yojo.codegen.mapper.MessageMapper}
 * extend this to implement schema- and message-specific logic.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class AbstractMapper {
    /**
     * Populates a {@link VariableProperties} instance from a raw YAML property definition.
     * <p>
     * Sets basic field metadata (name, type, description, example, validation attributes) and delegates
     * detailed type resolution to {@link #fillVariableProperties}.
     *
     * @param schemaName         name of the containing schema (used for logging and inner schema naming)
     * @param variableProperties target field container to populate
     * @param currentSchema      full schema map where this property is defined
     * @param schemas            global map of all known schemas (for $ref resolution)
     * @param propertyName       original YAML key (e.g., {@code "email"})
     * @param propertiesMap      raw YAML map for this field (e.g., {@code { type: string, format: email }})
     * @param processContext     current generation context (Spring Boot version, helper, etc.)
     * @param innerSchemas       accumulator for discovered inner schemas (e.g., anonymous objects, enums)
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
        // ⬇️ const → defaultProperty
        String constValue = getStringValueIfExistOrElseNull("const", propertiesMap);
        if (constValue != null) {
            variableProperties.setDefaultProperty(constValue);
        } else {
            variableProperties.setDefaultProperty(getStringValueIfExistOrElseNull(DEFAULT, propertiesMap));
        }
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
     * Resolves the concrete Java type and metadata for a field based on its raw YAML definition.
     * <p>
     * Handles:
     * <ul>
     *   <li>Maps ({@code additionalProperties})</li>
     *   <li>Arrays ({@code type: array}) with realization and nested types</li>
     *   <li>References ({@code $ref})</li>
     *   <li>Plain/inner objects</li>
     *   <li>Enums</li>
     *   <li>{@code format: existing} for external classes</li>
     *   <li>Polymorphic constructs ({@code oneOf}, {@code allOf}, {@code anyOf})</li>
     * </ul>
     *
     * @param schemaName         name of the containing schema
     * @param variableProperties field container to populate
     * @param currentSchema      current schema map
     * @param schemas            global schema registry
     * @param propertyName       field name in YAML
     * @param propertiesMap      raw field definition
     * @param processContext     generation context
     * @param innerSchemas       accumulator for inner schemas
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
        if (propertiesMap.isEmpty()) {
            variableProperties.setType(OBJECT_TYPE);
            variableProperties.setValid(false);
            return;
        }
        String type = getStringValueIfExistOrElseNull(TYPE, propertiesMap);
        String format = getStringValueIfExistOrElseNull(FORMAT, propertiesMap);
        if (getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null) {
            fillMapProperties(variableProperties, currentSchema, schemas, propertiesMap, commonPackage);
            return;
        }
        if (OBJECT_TYPE.equalsIgnoreCase(type) && format != null &&
            JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(format)) {
            variableProperties.setFormat(format);
            return;
        }
        variableProperties.setType(capitalize(type != null ? type : OBJECT_TYPE));
        if (ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillArrayProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillReferenceProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
        } else if (OBJECT_TYPE.equals(variableProperties.getType()) &&
                   getStringValueIfExistOrElseNull(PROPERTIES, propertiesMap) != null) {
            fillObjectProperties(schemaName, variableProperties, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
        } else if ((OBJECT_TYPE.equals(variableProperties.getType()) || STRING.equals(variableProperties.getType())) &&
                   getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            fillEnumProperties(schemaName, variableProperties, propertyName, propertiesMap, commonPackage, innerSchemas);
        } else if (OBJECT_TYPE.equals(variableProperties.getType()) &&
                   variableProperties.getPackageOfExisingObject() != null &&
                   variableProperties.getNameOfExisingObject() != null) {
            fillExistingObjectProperties(variableProperties);
        } else if (variableProperties.isPolymorph()) {
            System.out.println("FOUND POLYMORPHISM INSIDE SCHEMA! Schema: " + variableProperties.getName());
            List<Object> polymorphList = POLYMORPHS.stream()
                    .map(p -> propertiesMap.get(p))
                    .map(MapperUtil::castObjectToListObjects)
                    .flatMap(objects -> objects.stream())
                    .collect(Collectors.toList());
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
        } else {
            if (OBJECT_TYPE.equals(variableProperties.getType())) {
                variableProperties.setType(OBJECT_TYPE);
                variableProperties.setValid(false);
            }
            variableProperties.setFormat(format);
        }
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
     * Prepares a full import string for a class in the common package.
     *
     * @param commonPackage base common package (e.g., {@code "com.example.common;"})
     * @param className     simple class name
     * @return full import, e.g., {@code "com.example.common.MyClass;"}
     */
    private String prepareImport(String commonPackage, String className) {
        return commonPackage.replace(";", "." + className + ";");
    }

    /**
     * Fills map-related properties (e.g., {@code additionalProperties}) into {@link VariableProperties}.
     * Supports:
     * <ul>
     *   <li>Primitive value types (e.g., {@code Map<String, Integer>})</li>
     *   <li>Custom object values (via {@code $ref})</li>
     *   <li>Nested collections (e.g., {@code Map<String, List<User>>})</li>
     *   <li>Custom key types (e.g., {@code Map<UUID, ...>})</li>
     *   <li>{@code realization} for map initialization</li>
     * </ul>
     *
     * @param variableProperties target field
     * @param currentSchema      current schema map
     * @param schemas            global schemas
     * @param propertiesMap      field definition (must contain {@code additionalProperties})
     * @param commonPackage      base package for imports
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
        if (type != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(type)) {
            System.out.println("CORRECT TYPE!");
            if (format != null) {
                variableProperties.setType(format(MAP_CUSTOM_TYPE,
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(format),
                        JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(type)));
            } else {
                variableProperties.setType(format(MAP_TYPE, JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(type)));
            }
        } else if (OBJECT.equals(type) && referencedObject == null) {
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
        } else if (referencedObject != null && (schemas.containsKey(refReplace(referencedObject)) || currentSchema == schemas)) {
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
        } else if (ARRAY.equals(type) && getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, additionalPropertiesMap) != null) {
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
        } else if (ARRAY.equals(type) && getStringValueIfExistOrElseNull(PACKAGE, additionalPropertiesMap) != null) {
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
     * Configures {@link VariableProperties} for {@code format: existing} references.
     * Sets type to the simple class name and adds the required import.
     *
     * @param variableProperties field to configure
     */
    private static void fillExistingObjectProperties(VariableProperties variableProperties) {
        variableProperties.setType(capitalize(variableProperties.getNameOfExisingObject()));
        variableProperties.addRequiredImports(variableProperties.getPackageOfExisingObject()
                .concat(".")
                .concat(capitalize(variableProperties.getNameOfExisingObject()))
                .concat(";"));
    }

    /**
     * Processes enum fields and registers the corresponding enum schema in {@code innerSchemas}.
     * <p>
     * If {@code x-enumNames} is present, generates an enum with description field; otherwise, a plain enum.
     *
     * @param variableProperties field container
     * @param propertyName       YAML key
     * @param propertiesMap      raw enum definition
     * @param commonPackage      base package
     * @param innerSchemas       accumulator
     */
    private void fillEnumProperties(String schemaName,
                                    VariableProperties variableProperties,
                                    String propertyName,
                                    Map<String, Object> propertiesMap,
                                    String commonPackage,
                                    Map<String, Object> innerSchemas) {
        System.out.println("ENUMERATION FOUND: " + propertyName + " in " + schemaName);
        String enumClassName;
        if (schemaName.equalsIgnoreCase(propertyName)) {
            enumClassName = capitalize(propertyName);
        } else {
            enumClassName = capitalize(schemaName) + capitalize(propertyName);
        }

        fillEnumSchema(enumClassName, propertiesMap, innerSchemas);

        variableProperties.setType(enumClassName);
        variableProperties.setValid(false);
        variableProperties.setEnumNames(null);
        variableProperties.setEnumeration(null);
        variableProperties.setEnum(true);
        variableProperties.addRequiredImports(prepareImport(commonPackage, enumClassName));
    }

    /**
     * Processes inner/anonymous object schemas and registers them for separate DTO generation.
     * <p>
     * Generates a unique name (e.g., {@code ParentSchemaPropertyName}) and stores the synthetic schema
     * in {@code innerSchemas}.
     *
     * @param variableProperties field container
     * @param propertyName       YAML key
     * @param propertiesMap      raw object definition
     * @param commonPackage      base package
     * @param innerSchemas       accumulator
     */
    private void fillObjectProperties(String schemaName,
                                      VariableProperties variableProperties,
                                      Map<String, Object> schemas,
                                      String propertyName,
                                      Map<String, Object> propertiesMap,
                                      String commonPackage,
                                      Map<String, Object> innerSchemas) {
        String propName = capitalize(schemaName) + capitalize(propertyName);
        fillInnerSchema(variableProperties, propName, propertiesMap, commonPackage, innerSchemas);
    }

    /**
     * Resolves {@code $ref} references and populates field type/imports accordingly.
     * <p>
     * Handles recursive type resolution (e.g., {@code $ref} to scalar formats), detects referenced enums,
     * and adds appropriate imports (local or external via {@code package}).
     *
     * @param schemaName         current schema name
     * @param variableProperties field to configure
     * @param currentSchema      current schema map
     * @param schemas            global schema registry
     * @param propertyName       YAML key
     * @param propertiesMap      field definition (must contain {@code $ref})
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
            System.out.println();
            System.out.println("Start Recursive fillProperties " + propertyName);
            System.out.println();
            fillProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, stringObjectMap, processContext, innerSchemas);
        }

        if (variableProperties.getType() == null || OBJECT_TYPE.equals(variableProperties.getType())) {
            String schemaKey = refReplace(referenceObject); // точное имя ключа в schemas
            String className = capitalize(schemaKey);       // имя класса в Java — всегда с заглавной

            if (getStringValueIfExistOrElseNull(schemaKey, schemas) != null
                && castObjectToMap(schemas.get(schemaKey)).entrySet().stream()
                        .anyMatch(p -> POLYMORPHS.contains(p.getKey()))) {
                System.out.println("SKIP INHERITANCE POLYMORPH INSIDE SCHEMA! " + className);
                variableProperties.setType(className);
                variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), className));
            } else {
                variableProperties.setType(className);
                String possibleReferencedEnum = getStringValueIfExistOrElseNull(schemaKey, schemas);
                if (possibleReferencedEnum != null) {
                    Map<String, Object> possibleEnumSchema = castObjectToMap(schemas.get(schemaKey));
                    if (!possibleEnumSchema.isEmpty() && getStringValueIfExistOrElseNull(ENUMERATION, possibleEnumSchema) != null) {
                        variableProperties.setValid(false);
                    }
                }
                String externalPackage = getStringValueIfExistOrElseNull("package", propertiesMap);
                if (externalPackage != null) {
                    String fullImport = externalPackage + "." + className + ";";
                    variableProperties.addRequiredImports(fullImport);
                } else {
                    // ✅ ВСЕГДА добавляем импорт для локальных классов
                    variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), className));
                }
            }
        }
    }

    /**
     * Processes array-type fields and configures collection type, element type, and realization.
     * <p>
     * Special case: structurally empty {@code items} (no {@code type}, {@code $ref}, {@code properties})
     * defaults to {@code List<Object>}.
     *
     * @param schemaName         current schema name
     * @param variableProperties field container
     * @param currentSchema      current schema map
     * @param schemas            global schemas
     * @param propertyName       YAML key
     * @param propertiesMap      field definition
     * @param processContext     generation context
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
        Set<String> structuralKeys = Set.of(FORMAT, REFERENCE, PROPERTIES, ENUMERATION, ITEMS);
        boolean isStructurallyEmpty = items.entrySet().stream()
                .noneMatch(e -> structuralKeys.contains(e.getKey()));
        if (isStructurallyEmpty) {
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
                    variableProperties.setItems(capitalize(refReplace(refValue)));
                }
            } else {
                variableProperties.setItems(capitalize(refReplace(refValue)));
            }
            fillCollectionType(variableProperties);
        } else {
            if (getStringValueIfExistOrElseNull(FORMAT, items) != null || getStringValueIfExistOrElseNull(TYPE, items) != null) {
                if (getStringValueIfExistOrElseNull(FORMAT, items) != null) {
                    if (getStringValueIfExistOrElseNull(FORMAT, items).equals(EXISTING)) {
                        fillCollectionWithExistingObject(variableProperties, items);
                    } else {
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
            String externalPackage = getStringValueIfExistOrElseNull("package", items);
            if (externalPackage != null) {
                variableProperties.addRequiredImports(externalPackage + "." + variableProperties.getItems() + ";");
            } else {
                variableProperties.addRequiredImports(prepareImport(processContext.getCommonPackage(), variableProperties.getItems()));
            }
        }
    }

    /**
     * Configures collection realization and import for {@code format: existing} arrays.
     *
     * @param variableProperties field container
     * @param items              {@code items} map from YAML
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
     * Registers an inner schema for subsequent DTO generation.
     * <p>
     * Normalizes the schema (ensures {@code type: object} and extracts actual properties) before registration.
     *
     * @param variableProperties field container
     * @param propertyName       proposed inner schema name
     * @param propertiesMap      raw inner schema definition
     * @param commonPackage      base package
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
        Map<String, Object> innerSchema = new LinkedHashMap<>(propertiesMap);
        if (!innerSchema.containsKey(TYPE)) {
            innerSchema.put(TYPE, OBJECT);
        }
        if (!innerSchema.containsKey(PROPERTIES) && !innerSchema.containsKey(ENUMERATION) && !innerSchema.containsKey(REFERENCE)) {
            Map<String, Object> actualProps = new LinkedHashMap<>();
            Iterator<Map.Entry<String, Object>> it = innerSchema.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                if (!(key.equals(TYPE) || key.equals(FORMAT) || key.equals(DESCRIPTION) ||
                      key.equals(EXAMPLE) || key.equals(DEFAULT) || key.equals(PATTERN) ||
                      key.equals(MIN_LENGTH) || key.equals(MAX_LENGTH) ||
                      key.equals(MINIMUM) || key.equals(MAXIMUM) ||
                      key.equals(DIGITS) ||
                      key.equals(REALIZATION) || key.equals(NAME) || key.equals(PACKAGE))) {
                    actualProps.put(key, entry.getValue());
                    it.remove();
                }
            }
            if (!actualProps.isEmpty()) {
                innerSchema.put(PROPERTIES, actualProps);
            }
        }
        innerSchemas.put(propertyName, innerSchema);
    }

    /**
     * Populates {@code messagesMap} from AsyncAPI 2.x {@code channels}/{@code publish}/{@code subscribe} sections.
     * <p>
     * Resolves payload definitions, extracts polymorphic payloads (e.g., {@code oneOf}) and creates synthetic
     * message definitions when needed.
     *
     * @param allContent     full AsyncAPI document
     * @param messagesMap    output map (schema name → payload)
     * @param excludeSchemas set of schema names to exclude from DTO generation
     * @param mapToMessage   channel operation map (e.g., {@code subscribe} or {@code publish})
     * @param channelName    channel key
     * @param channelType    {@code "subscribe"} or {@code "publish"}
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
                            messagesMap.put(messageName, Map.of(PAYLOAD, Map.of(TYPE, OBJECT, PROPERTIES, propertiesMap)));
                        } else {
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
                            messagesMap.put(messageName, Map.of(PAYLOAD, schemaMap));
                        }
                    }
                });
    }

    /**
     * Sets the final Java collection type string (e.g., {@code List<String>}) based on {@code collectionType}.
     *
     * @param variableProperties field container (must have {@code items} set)
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
     * Creates a synthetic schema map for an enum with descriptions (from {@code x-enumNames}).
     *
     * @param enums map: constant name → human-readable description
     * @return synthetic schema map suitable for inner schema registration
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
     * Creates a synthetic schema map for a plain enum (no description field).
     *
     * @param enums list of constant names
     * @return synthetic schema map
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
     * Applies validation annotations and imports based on {@code required}, validation groups, and field type.
     * <p>
     * Generates:
     * <ul>
     *   <li>{@code @NotNull}/{@code @NotBlank}/{@code @NotEmpty} for {@code required} fields</li>
     *   <li>{@code groups = {...}} variants if validation groups are configured</li>
     *   <li>{@code @Valid} for nested objects (excluding collections/enums)</li>
     * </ul>
     *
     * @param variableProperties field container (updated in-place)
     * @param currentSchema      schema map (to read {@code required}, {@code validationGroups}, etc.)
     * @param propertyName       field name (to check presence in {@code required})
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
     * Adds {@code @Valid} annotation and corresponding import if the field is a nested object.
     *
     * @param variableProperties field container
     * @param annotationSet      target annotation set (mutated)
     * @param importSet          target import set (mutated)
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

    /**
     * Registers an enum schema in {@code innerSchemas} based on raw YAML definition.
     * <p>
     * Delegates to {@link #fillByEnumWithDescription} or {@link #fillByEnum} depending on presence of
     * {@code x-enumNames}.
     *
     * @param propertyName  enum field name
     * @param propertiesMap raw enum definition
     * @param innerSchemas  accumulator
     */
    private static void fillEnumSchema(String enumSchemaName,
                                       Map<String, Object> propertiesMap,
                                       Map<String, Object> innerSchemas) {
        System.out.println(">>> ADDING ENUM TO INNER SCHEMAS: " + enumSchemaName);
        if (getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap) != null) {
            Map<String, Object> enumerationMap = castObjectToMap(propertiesMap.get(X_ENUM_NAMES));
            Map<String, Object> enums = new LinkedHashMap<>();
            Map<String, Object> enumWithDescription = fillByEnumWithDescription(enumerationMap);
            innerSchemas.put(uncapitalize(enumSchemaName), enumWithDescription);
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            List<String> enums = castObjectToList(propertiesMap.get(ENUMERATION));
            Map<String, Object> enumsMap = fillByEnum(enums);
            innerSchemas.put(uncapitalize(enumSchemaName), enumsMap);
        }
    }
}