package ru.yojo.codegen.mapper;

import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.exception.SchemaFillException;
import ru.yojo.codegen.util.MapperUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class AbstractMapper {


    private final Helper helper;

    public AbstractMapper(Helper helper) {
        this.helper = helper;
    }

    /**
     * Fill properties
     *
     * @param variableProperties variableProperties
     * @param currentSchema      currentSchema
     * @param schemas            schemas
     * @param propertyName       propertyName
     * @param propertiesMap      propertiesMap
     * @param commonPackage      commonPackage
     * @param innerSchemas       innerSchemas
     */
    public void fillProperties(String schemaName,
                               VariableProperties variableProperties,
                               Map<String, Object> currentSchema,
                               Map<String, Object> schemas,
                               String propertyName,
                               Map<String, Object> propertiesMap,
                               String commonPackage,
                               Map<String, Object> innerSchemas) {
            variableProperties.setName(uncapitalize(propertyName));
            variableProperties.setDefaultProperty(getStringValueIfExistOrElseNull(DEFAULT, propertiesMap));
            variableProperties.setType(capitalize(getStringValueIfExistOrElseNull(TYPE, propertiesMap)));
            variableProperties.setPrimitive(getStringValueIfExistOrElseNull(PRIMITIVE, propertiesMap));
            variableProperties.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, propertiesMap));
            variableProperties.setExample(getStringValueIfExistOrElseNull(EXAMPLE, propertiesMap));
            variableProperties.setTitle(getStringValueIfExistOrElseNull(TITLE, propertiesMap));
            variableProperties.setDigits(getStringValueIfExistOrElseNull(DIGITS, propertiesMap));
            variableProperties.setFormat(getStringValueIfExistOrElseNull(FORMAT, propertiesMap));
            variableProperties.setPattern(getStringValueIfExistOrElseNull(PATTERN, propertiesMap));
            variableProperties.setMinMaxLength(getStringValueIfExistOrElseNull(MIN_LENGTH, propertiesMap), getStringValueIfExistOrElseNull(MAX_LENGTH, propertiesMap));
            variableProperties.setMinimum(getStringValueIfExistOrElseNull(MINIMUM, propertiesMap));
            variableProperties.setMaximum(getStringValueIfExistOrElseNull(MAXIMUM, propertiesMap));
            variableProperties.setEnumeration(getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap));
            variableProperties.setEnumNames(getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap));
            variableProperties.setPackageOfExisingObject(getStringValueIfExistOrElseNull(PACKAGE, propertiesMap));
            variableProperties.setNameOfExisingObject(getStringValueIfExistOrElseNull(NAME, propertiesMap));
            variableProperties.setPolymorph(
                    !POLYMORPHS.stream()
                            .map(p -> propertiesMap.get(p))
                            .map(MapperUtil::castObjectToListObjects)
                            .flatMap(objects -> objects.stream())
                            .collect(Collectors.toList()).isEmpty());

            fillVariableProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
            fillRequiredAnnotationsAndImports(variableProperties, currentSchema, propertyName);
    }

    /**
     * filling from $ref Objects
     *
     * @param variableProperties variableProperties
     * @param currentSchema      currentSchema
     * @param schemas            schemas
     * @param propertyName       propertyName
     * @param propertiesMap      propertiesMap
     * @param commonPackage      commonPackage
     * @param innerSchemas       innerSchemas
     */
    public void fillVariableProperties(String schemaName,
                                       VariableProperties variableProperties,
                                       Map<String, Object> currentSchema,
                                       Map<String, Object> schemas,
                                       String propertyName,
                                       Map<String, Object> propertiesMap,
                                       String commonPackage,
                                       Map<String, Object> innerSchemas) {
        if (ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillArrayProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            fillReferenceProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
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
        } else if (getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null) {
            fillMapProperties(variableProperties, currentSchema, schemas, propertiesMap, commonPackage);
        } else if (variableProperties.isPolymorph()) {
            System.out.println("FOUND POLYMORPHISM INSIDE SCHEMA! Schema: " + variableProperties.getName());

            List<Object> polymorphList = POLYMORPHS.stream()
                    .map(p -> propertiesMap.get(p))
                    .map(MapperUtil::castObjectToListObjects)
                    .flatMap(objects -> objects.stream())
                    .map(MapperUtil::castObjectToMap)
                    .flatMap(map -> map.entrySet().stream())
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            System.out.println("POLYMORPH LIST: " + polymorphList);

            //Getting merged polymorph properties
            Map<String, Object> mergedProperties = polymorphList.stream()
                    .flatMap(ref -> castObjectToMap(schemas.get(refReplace(ref.toString()))).entrySet().stream())
                    .filter(en -> en.getKey().equals(PROPERTIES))
                    .map(pr -> castObjectToMap(pr.getValue()))
                    .flatMap(map -> map.entrySet().stream())
                    .distinct()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (existing, replacement) -> existing));

            String className = capitalize(propertyName);
            for (int i = 0; i < polymorphList.size(); i++) {
                className = className + refReplace(polymorphList.get(i).toString());
            }
            variableProperties.setType(className);
            variableProperties.addRequiredImports(prepareImport(commonPackage, className));

            Map<String, Object> preparedMergedPolymorphSchema = Map.of(className, Map.of(TYPE, OBJECT, PROPERTIES, mergedProperties));
            System.out.println(preparedMergedPolymorphSchema);
            innerSchemas.putAll(preparedMergedPolymorphSchema);
        }
    }

    //If mapped from schemas not need to add common imports
    private String prepareImport(String commonPackage, String className) {
        if (!helper.isMappedFromSchemas() && helper.isMappedFromMessages()) {
            return commonPackage.replace(";", "." + className + ";");
        } else {
            return null;
        }
    }

    private void fillMapProperties(VariableProperties variableProperties, Map<String, Object> currentSchema, Map<String, Object> schemas, Map<String, Object> propertiesMap, String commonPackage) {
        System.out.println();
        System.out.println("ADDITIONAL PROPERTIES");
        String type = getStringValueIfExistOrElseNull(TYPE, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
        String referencedObject = getStringValueIfExistOrElseNull(REFERENCE, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
        variableProperties.addRequiredImports(MAP_IMPORT);
        variableProperties.setRealisation(getStringValueIfExistOrElseNull(REALIZATION, propertiesMap));
        variableProperties.setValid(false);
        //Fill with javaTypes
        if (type != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(type)) {
            System.out.println("CORRECT TYPE!");
            variableProperties.setType(format(MAP_TYPE, JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(type)));
        } else if (OBJECT.equals(type) && referencedObject == null) {
            variableProperties.setType(format(MAP_TYPE, OBJECT_TYPE));
            //Fill with custom object
        } else if (referencedObject != null && (schemas.containsKey(refReplace(referencedObject)) || currentSchema == schemas)) {
            String refObjectName = refReplace(referencedObject);
            System.out.println("FOUND CUSTOM OBJECT! " + refObjectName);
            if (ARRAY.equals(type)) {
                String collectionType = getStringValueIfExistOrElseNull(FORMAT, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
                if (collectionType != null) {
                    variableProperties.setCollectionType(collectionType);
                }
                String javaType = getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
                if (javaType != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(javaType)) {
                    variableProperties.setItems(JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(javaType).toString());
                    fillCollectionType(variableProperties);
                    variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
                } else {
                    variableProperties.setItems(refObjectName);
                    fillCollectionType(variableProperties);
                    variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
                    variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
                }
            } else {
                variableProperties.setType(format(MAP_TYPE, refObjectName));
                variableProperties.addRequiredImports(prepareImport(commonPackage, refObjectName));
            }
        } else if (ARRAY.equals(type) && getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES))) != null) {
            String collectionType = getStringValueIfExistOrElseNull(FORMAT, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
            if (collectionType != null) {
                variableProperties.setCollectionType(collectionType);
            }
            variableProperties.setItems(JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.get(getStringValueIfExistOrElseNull(ADDITIONAL_FORMAT, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)))).toString());
            fillCollectionType(variableProperties);
            variableProperties.setType(format(MAP_TYPE, format(variableProperties.getType())));
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

    private void fillEnumProperties(VariableProperties variableProperties, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        System.out.println("ENUMERATION FOUND: " + propertyName);
        fillEnumSchema(propertyName, propertiesMap, innerSchemas);
        variableProperties.setType(capitalize(propertyName));
        variableProperties.setValid(false);
        variableProperties.setEnumNames(null);
        variableProperties.setEnumeration(null);
        variableProperties.setEnum(true);
        variableProperties.addRequiredImports(prepareImport(commonPackage, capitalize(propertyName)));
    }

    private void fillObjectProperties(String schemaName, VariableProperties variableProperties, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        String propName;
        if (schemas.containsKey(propertyName) || schemas.containsKey(capitalize(propertyName)) ||
                innerSchemas.containsKey(propertyName) || innerSchemas.containsKey(capitalize(propertyName))) {
            propName = capitalize(schemaName).concat(capitalize(propertyName));
        } else {
            propName = propertyName;
        }
        fillInnerSchema(variableProperties, propName, propertiesMap, commonPackage, innerSchemas);
    }

    private void fillReferenceProperties(String schemaName, VariableProperties variableProperties, Map<String, Object> currentSchema, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        String referenceObject = propertiesMap.get(REFERENCE).toString();
        Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
        String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
        if (objectType != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(objectType)) {
            //Recursive Fill
            System.out.println();
            System.out.println("Start Recursive fillProperties " + propertyName);
            System.out.println();
            fillProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, stringObjectMap, commonPackage, innerSchemas);
        }
        if (variableProperties.getType() == null || OBJECT_TYPE.equals(variableProperties.getType())) {
            String refReplace = refReplace(referenceObject);
            if (getStringValueIfExistOrElseNull(refReplace, schemas) != null
                    && castObjectToMap(schemas.get(refReplace)).entrySet().stream()
                    .anyMatch(p -> POLYMORPHS.contains(p.getKey()))) {
                System.out.println("SKIP INHIRITANCE POLYMORPH INSIDE SCHEMA! " + refReplace);
                variableProperties.setType(refReplace);
                variableProperties.addRequiredImports(prepareImport(commonPackage, refReplace));
            } else {
                variableProperties.setType(refReplace);
                String possibleReferencedEnum = getStringValueIfExistOrElseNull(refReplace, schemas);
                if (possibleReferencedEnum != null) {
                    Map<String, Object> possibleEnumSchema = castObjectToMap(schemas.get(refReplace));
                    if (!possibleEnumSchema.isEmpty() && getStringValueIfExistOrElseNull(ENUMERATION, possibleEnumSchema) != null) {
                        variableProperties.setValid(false);
                    }
                }
                if (Character.isUpperCase(refReplace.charAt(0))) {
                    variableProperties.addRequiredImports(prepareImport(commonPackage, refReplace));
                }
            }
        }
    }

    private void fillArrayProperties(String schemaName, VariableProperties variableProperties, Map<String, Object> currentSchema, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        Map<String, Object> items = castObjectToMap(propertiesMap.get(ITEMS));
        variableProperties.setRealisation(getStringValueIfExistOrElseNull(REALIZATION, items));
        String refValue = getStringValueIfExistOrElseNull(REFERENCE, items);
        String collectionFormat = getStringValueIfExistOrElseNull(FORMAT, propertiesMap);
        boolean javaType = false;
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
                //Fill java format atributes array
                if (getStringValueIfExistOrElseNull(FORMAT, items) != null) {
                    variableProperties.setItems(items.get(FORMAT).toString());
                    variableProperties.setFormat(items.get(FORMAT).toString());
                } else {
                    if (getStringValueIfExistOrElseNull(PROPERTIES, items) != null) {
                        fillObjectProperties(schemaName, variableProperties, schemas, propertyName, items, commonPackage, innerSchemas);
                    } else {
                        variableProperties.setItems(items.get(TYPE).toString());
                        variableProperties.setFormat(items.get(TYPE).toString());
                    }
                }
            } else {
                fillProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, items, commonPackage, innerSchemas);
                variableProperties.setItems(capitalize(propertyName));
                fillCollectionType(variableProperties);
            }
        }
        if (variableProperties.getItems() != null &&
                !JAVA_DEFAULT_TYPES.contains(variableProperties.getItems()) &&
                !OBJECT_TYPE.equals(variableProperties.getItems()) && !javaType) {
            variableProperties.addRequiredImports(prepareImport(commonPackage, variableProperties.getItems()));
        }
    }

    private void fillInnerSchema(VariableProperties variableProperties,
                                 String propertyName,
                                 Map<String, Object> propertiesMap,
                                 String commonPackage,
                                 Map<String, Object> innerSchemas) {
        System.out.println("FOUND INNER SCHEMA!!! " + propertyName);
        variableProperties.setType(capitalize(propertyName));
        variableProperties.addRequiredImports(prepareImport(commonPackage, capitalize(propertyName)));
        innerSchemas.put(propertyName, propertiesMap);
    }

    public static void fillMessageFromChannel(Map<String, Object> allContent, Map<String, Object> messagesMap, Set<String> excludeSchemas, Map<String, Object> mapToMessage, String channelName, String channelType) {
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

                            //Here create propertiesMap from all referenced objects properties
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
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                            (existing, replacement) -> existing));

                            //Here put in the properties all from referenced schemas
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

                            //Here fill message map by structure:
                            // MessageName:
                            //      payload:
                            //          type: object
                            //          properties:
                            //              content here
                            messagesMap.put(messageName, Map.of(PAYLOAD, Map.of(TYPE, OBJECT, PROPERTIES, propertiesMap)));
                        } else {

                            //Here prepare map with merged properties
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
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing));

                            //Here fill message map by structure:
                            // MessageName:
                            //      payload:
                            //          content here
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

    private static void fillEnumSchema(String propertyName, Map<String, Object> propertiesMap, Map<String, Object> innerSchemas) {
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

    private static Map<String, Object> fillByEnumWithDescription(Map<String, Object> enums) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> vp = new LinkedHashMap<>();
        Map<String, Object> type = new LinkedHashMap<>();
        Map<String, Object> properties = new LinkedHashMap<>() {
        };
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
        Map<String, Object> properties = new LinkedHashMap<>() {
        };
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

    public static void fillRequiredAnnotationsAndImports(VariableProperties variableProperties, Map<String, Object> currentSchema, String propertyName) {
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
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION));
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

                    //VALIDATION GROUPS FOR JAVA OBJECTS
                    if (validationFields.contains(propertyName) && finalGroups != null) {
                        importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                        annotation = annotation + finalGroups;
                        annotationSet.add(annotation);
                        importSet.addAll(validationGroupsImports.stream().map(vi -> vi.concat(";")).collect(Collectors.toSet()));
                    } else {
                        importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
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

    private static void validAnnotationCheck(VariableProperties variableProperties, Set<String> annotationSet, Set<String> importSet) {
        if (variableProperties.isEnum() == false &&
                variableProperties.getType() != null &&
                !JAVA_DEFAULT_TYPES.contains(variableProperties.getType()) &&
                variableProperties.isValid()) {
            importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION));
            annotationSet.add(VALID_ANNOTATION);
        }
    }
}
