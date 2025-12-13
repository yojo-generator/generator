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
 */
@SuppressWarnings("all")
public class AbstractMapper {

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
            fillEnumProperties(variableProperties, propertyName, propertiesMap, commonPackage, innerSchemas);
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

    private String prepareImport(String commonPackage, String className) {
        return commonPackage.replace(";", "." + className + ";");
    }

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

    private static void fillExistingObjectProperties(VariableProperties variableProperties) {
        variableProperties.setType(capitalize(variableProperties.getNameOfExisingObject()));
        variableProperties.addRequiredImports(variableProperties.getPackageOfExisingObject()
                .concat(".")
                .concat(capitalize(variableProperties.getNameOfExisingObject()))
                .concat(";"));
    }

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

    private static void fillCollectionType(VariableProperties variableProperties) {
        switch (variableProperties.getCollectionType()) {
            case "list":
                variableProperties.setType(format(refReplace(LIST_TYPE), variableProperties.getItems()));
                break;
            case "set":
                variableProperties.setType(format(refReplace(SET_TYPE), variableProperties.getItems()));
        }
    }

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
}