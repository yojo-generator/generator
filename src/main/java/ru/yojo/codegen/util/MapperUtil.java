package ru.yojo.codegen.util;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.exception.SchemaFillException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

@SuppressWarnings("all")
public class MapperUtil {

    public static Set<String> getSetValueIfExistsOrElseEmptySet(String value, Map<String, Object> schemaMap) {
        Set<String> values = new HashSet<>();
        if (schemaMap.containsKey(value)) {
            values.addAll((ArrayList<String>) schemaMap.get(value));
        }
        return values;
    }

    public static String getStringValueIfExistOrElseNull(String constant, Map<String, Object> map) {
        if (map.containsKey(constant)) {
            Object value = map.getOrDefault(constant, null);
            if (value != null) {
                return value.toString();
            } else {
                return null;
            }
        }
        return null;
    }

    public static Map<String, Object> castObjectToMap(Object map) {
        if (map == null) {
            return new LinkedHashMap<>();
        }
        return (Map<String, Object>) map;
    }

    public static ArrayList<String> castObjectToList(Object list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) list;
    }

    public static ArrayList<Object> castObjectToListObjects(Object list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return (ArrayList<Object>) list;
    }

    public static String refReplace(String ref) {
        return capitalize(ref.replaceAll(".+/", ""));
    }

    public static String generateSetter(String type, String variableName) {
        return format(SETTER, capitalize(variableName), capitalize(type), variableName, variableName, variableName);
    }

    public static String generateGetter(String type, String variableName) {
        return format(GETTER, capitalize(type), capitalize(variableName), variableName);
    }

    public static String generateEnumConstructor(String enumClassName, String type, String variableName) {
        return format(ENUM_CONSTRUCTOR, enumClassName, capitalize(type), variableName);
    }

    public static StringBuilder getClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_CLASS)
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getEnumClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_ENUM)
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getImplementationClassBuilder(String schemaName, Set<String> implementsFrom) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_CLASS)
                .append(schemaName)
                .append(SPACE)
                .append(IMPLEMENTS)
                .append(SPACE);
        implementsFrom.forEach(impl -> {
            stringBuilder.append(impl).append(",");
        });

        return new StringBuilder(stringBuilder.toString().replaceFirst(".$", "").concat(" {"));
    }

    public static StringBuilder getExtendsClassBuilder(String schemaName, String extendsClass) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_CLASS)
                .append(schemaName)
                .append(SPACE)
                .append(EXTENDS)
                .append(SPACE)
                .append(extendsClass)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getExtendsWithImplementationClassBuilder(String schemaName, String extendsClass, Set<String> implementsFrom) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_CLASS)
                .append(schemaName)
                .append(SPACE)
                .append(EXTENDS)
                .append(SPACE)
                .append(extendsClass)
                .append(SPACE)
                .append(IMPLEMENTS)
                .append(SPACE);
        implementsFrom.forEach(impl -> {
            stringBuilder.append(impl).append(",");
        });

        return new StringBuilder(stringBuilder.toString().replaceFirst(".$", "").concat(" {"));
    }

    /**
     * The method adds to a JavaDoc file
     *
     * @param stringBuilder StringBuilder
     * @param description   Description
     * @param example       Example
     */
    public static void generateJavaDoc(StringBuilder stringBuilder, String description, String example) {
        if (isNoneEmpty(description) || isNoneEmpty(example)) {
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_START);
            if (isNotBlank(description)) {
                stringBuilder.append(lineSeparator()).append(format(JAVA_DOC_LINE, description));
            }
            if (isNotBlank(example)) {
                stringBuilder.append(lineSeparator()).append(format(JAVA_DOC_EXAMPLE, example));
            }
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_END);
        }
    }

    /**
     * The method adds to a JavaDoc file
     *
     * @param stringBuilder StringBuilder
     * @param description   Description or Title
     */
    public static void generateClassJavaDoc(StringBuilder stringBuilder, String description) {
        if (isNoneEmpty(description)) {
            stringBuilder.insert(0, lineSeparator());
            stringBuilder.insert(0, JAVA_DOC_CLASS_END);
            stringBuilder.insert(0, lineSeparator());
            stringBuilder.insert(0, format(JAVA_DOC_CLASS_LINE, description));
            stringBuilder.insert(0, lineSeparator());
            stringBuilder.insert(0, JAVA_DOC_CLASS_START);
        }
    }

    public static String getPackage(String packageLocation, String outputDirectoryName, String messagePackageImport) {
        return String.join(".", packageLocation, outputDirectoryName, messagePackageImport);
    }

    /**
     * The method adds the Size annotation
     *
     * @param minLength minLength
     * @param maxLength maxLength
     * @return return Size annotation
     */
    public static String generateSizeAnnotation(String minLength, String maxLength) {
        if (isNotBlank(minLength) && isNotBlank(maxLength)) {
            return format(SIZE_MIN_MAX_ANNOTATION, minLength, maxLength);
        } else if (isNotBlank(minLength)) {
            return format(SIZE_MIN_ANNOTATION, minLength);
        } else {
            return format(SIZE_MAX_ANNOTATION, maxLength);
        }
    }

    public static String generateMinAnnotation(String min) {
        return format(MINIMUM_ANNOTATION, min);
    }

    public static String generateMaxAnnotation(String max) {
        return format(MAXIMUM_ANNOTATION, max);
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
    public static void fillProperties(VariableProperties variableProperties, Map<String, Object> currentSchema, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
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

        recursivelyFillProperties(variableProperties, currentSchema, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
        fillRequiredAnnotationsAndImports(variableProperties, currentSchema, propertyName);
    }

    /**
     * Recursive Filling from $ref Objects
     *
     * @param variableProperties variableProperties
     * @param currentSchema      currentSchema
     * @param schemas            schemas
     * @param propertyName       propertyName
     * @param propertiesMap      propertiesMap
     * @param commonPackage      commonPackage
     * @param innerSchemas       innerSchemas
     */
    public static void recursivelyFillProperties(VariableProperties variableProperties, Map<String, Object> currentSchema, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        if (ARRAY.equals(uncapitalize(variableProperties.getType()))) {
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
                        variableProperties.setItems(items.get(TYPE).toString());
                        variableProperties.setFormat(items.get(TYPE).toString());
                    }
                } else {
                    fillProperties(variableProperties, currentSchema, schemas, propertyName, items, commonPackage, innerSchemas);
                    variableProperties.setItems(capitalize(propertyName));
                    fillCollectionType(variableProperties);
                }
            }
            if (variableProperties.getItems() != null &&
                    !JAVA_DEFAULT_TYPES.contains(variableProperties.getItems()) &&
                    !OBJECT_TYPE.equals(variableProperties.getItems()) && !javaType) {
                variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + variableProperties.getItems() + ";"));
            }
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.equals(uncapitalize(variableProperties.getType()))) {
            String referenceObject = propertiesMap.get(REFERENCE).toString();
            Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
            String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
            if (objectType != null && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(objectType)) {
                //Recursive Fill
                System.out.println();
                System.out.println("Start Recursive fillProperties " + propertyName);
                System.out.println();
                fillProperties(variableProperties, currentSchema, schemas, propertyName, stringObjectMap, commonPackage, innerSchemas);
            }
            if (variableProperties.getType() == null || OBJECT_TYPE.equals(variableProperties.getType())) {
                String refReplace = refReplace(referenceObject);
                variableProperties.setType(refReplace);
                String possibleReferencedEnum = getStringValueIfExistOrElseNull(refReplace, schemas);
                if (possibleReferencedEnum != null) {
                    Map<String, Object> possibleEnumSchema = castObjectToMap(schemas.get(refReplace));
                    if (!possibleEnumSchema.isEmpty() && getStringValueIfExistOrElseNull(ENUMERATION, possibleEnumSchema) != null) {
                        variableProperties.setValid(false);
                    }
                }
                if (Character.isUpperCase(refReplace.charAt(0))) {
                    variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + refReplace + ";"));
                }
            }
        } else if (OBJECT_TYPE.equals(variableProperties.getType()) &&
                getStringValueIfExistOrElseNull(PROPERTIES, propertiesMap) != null) {
            System.out.println("FOUND INNER SCHEMA!!! " + propertyName);
            variableProperties.setType(capitalize(propertyName));
            variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + capitalize(propertyName) + ";"));
            innerSchemas.put(propertyName, propertiesMap);
        } else if ((OBJECT_TYPE.equals(variableProperties.getType()) || STRING.equals(variableProperties.getType())) &&
                getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            System.out.println("ENUMERATION FOUND");
            fillEnumSchema(propertyName, propertiesMap, innerSchemas);
            variableProperties.setType(capitalize(propertyName));
            variableProperties.setValid(false);
            variableProperties.setEnumNames(null);
            variableProperties.setEnumeration(null);
            variableProperties.setEnum(true);
            variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + capitalize(propertyName) + ";"));
        } else if (getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null) {
            System.out.println();
            System.out.println("ADDITIONAL PROPERTIES");
            String type = getStringValueIfExistOrElseNull(TYPE, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
            String referencedObject = getStringValueIfExistOrElseNull(REFERENCE, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES)));
            variableProperties.getRequiredImports().add(MAP_IMPORT);
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
                        variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + refObjectName + ";"));
                    }
                } else {
                    variableProperties.setType(format(MAP_TYPE, refObjectName));
                    variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + refObjectName + ";"));
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
    }

    public static void fillMessage(Map<String, Object> allContent, Map<String, Object> messagesMap, Set<String> excludeSchemas, Map<String, Object> mapToMessage, String channelName, String channelType) {
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

    /**
     * Method filling required annotations and imports
     *
     * @param variableProperties variableProperties
     * @param currentSchema      currentSchema
     * @param propertyName       propertyName
     */
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
            if (variableProperties.isEnum() == false &&
                    variableProperties.getType() != null &&
                    !JAVA_DEFAULT_TYPES.contains(variableProperties.getType()) &&
                    variableProperties.isValid()) {
                importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION));
                annotationSet.add(VALID_ANNOTATION);
            }
        });
        if (variableProperties.isEnum() == false &&
                variableProperties.getType() != null &&
                !JAVA_DEFAULT_TYPES.contains(variableProperties.getType()) &&
                variableProperties.isValid()) {
            importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION));
            annotationSet.add(VALID_ANNOTATION);
        }
        variableProperties.getRequiredImports().addAll(importSet);
        variableProperties.getAnnotationSet().addAll(annotationSet);
    }

    public static void buildLombokAnnotations(LombokProperties lombokProperties, Set<String> requiredImports, StringBuilder lombokAnnotationBuilder) {
        lombokAnnotationBuilder
                .append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION)
                .append(lineSeparator());
        requiredImports.add(LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT);

        if (lombokProperties.getAccessors().isEnable()) {
            String accessors = fetchAccessors(lombokProperties);

            lombokAnnotationBuilder.append(accessors)
                    .append(lineSeparator());
            requiredImports.add(LOMBOK_ACCESSORS_IMPORT);
        }
        if (lombokProperties.allArgsConstructor()) {
            lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION)
                    .append(lineSeparator());
            requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT);
        }
        if (lombokProperties.getEqualsAndHashCode() != null && lombokProperties.getEqualsAndHashCode().isEnable()) {
            requiredImports.add(LOMBOK_EQUALS_AND_HASH_CODE_IMPORT);
            if (lombokProperties.getEqualsAndHashCode().getCallSuper() != null) {
                if (lombokProperties.getEqualsAndHashCode().getCallSuper()) {
                    lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION)
                            .append(lineSeparator());
                } else {
                    lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION)
                            .append(lineSeparator());
                }
            } else {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_ANNOTATION)
                        .append(lineSeparator());
            }
        }
    }

    private static String fetchAccessors(LombokProperties lombokProperties) {
        if (lombokProperties.getAccessors().isChain() && lombokProperties.getAccessors().isFluent()) {
            return LOMBOK_ACCESSORS_ANNOTATION;
        }
        if (lombokProperties.getAccessors().isChain()) {
            return LOMBOK_ACCESSORS_CHAIN_ANNOTATION;
        }
        if (lombokProperties.getAccessors().isFluent()) {
            return LOMBOK_ACCESSORS_FLUENT_ANNOTATION;
        }
        return LOMBOK_ACCESSORS_EMPTY_ANNOTATION;
    }

    public static String finishBuild(StringBuilder stringBuilder, Set<String> requiredImports, String packageName) {
        StringBuilder importBuilder = new StringBuilder();
        requiredImports.forEach(requiredImport -> {
            importBuilder
                    .append(IMPORT)
                    .append(requiredImport)
                    .append(lineSeparator());
        });
        stringBuilder.insert(0, importBuilder.append(lineSeparator()));

        stringBuilder.insert(0, new StringBuilder("package ")
                .append(packageName)
                .append(lineSeparator())
                .append(lineSeparator()));

        return stringBuilder
                .append(lineSeparator())
                .append("}")
                .toString();
    }

    public static StringBuilder prepareStringBuilder(Set<String> requiredImports, Set<String> implementsFrom, String extendsFrom, String schemaName, Set<String> importSet, FillParameters fillParameters) {
        StringBuilder stringBuilder;
        if (!implementsFrom.isEmpty() && isNotBlank(extendsFrom)) {
            stringBuilder = getExtendsWithImplementationClassBuilder(schemaName, extendsFrom, implementsFrom);
        } else if (!implementsFrom.isEmpty() && isBlank(extendsFrom)) {
            stringBuilder = getImplementationClassBuilder(schemaName, implementsFrom);
        } else if (implementsFrom.isEmpty() && isNotBlank(extendsFrom)) {
            stringBuilder = getExtendsClassBuilder(schemaName, extendsFrom);
        } else {
            stringBuilder = getClassBuilder(schemaName);
        }

        requiredImports.addAll(importSet);
        stringBuilder
                .append(fillParameters.toWrite())
                .append(lineSeparator());
        return stringBuilder;
    }

    /*
     *
     * Methods from Apache lang utils
     *
     */
    public static String capitalize(final String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        return checkPoints(firstCodepoint, newCodePoint, str, new int[strLen], strLen);
    }

    public static String uncapitalize(final String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        return checkPoints(firstCodepoint, newCodePoint, str, new int[strLen], strLen);
    }

    private static String checkPoints(int firstCodepoint, int newCodePoint, String str, int[] strLen, int strLen1) {
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int[] newCodePoints = strLen; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen1; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isAnyEmpty(final CharSequence... css) {
        if (isEmpty(css)) {
            return false;
        }
        for (final CharSequence cs : css) {
            if (isEmpty(cs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoneEmpty(final CharSequence... css) {
        return !isAnyEmpty(css);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    public static Map<String, Object> getSchemaByName(Map<String, Object> content, String schemaName) {
        return castObjectToMap(
                castObjectToMap(
                        castObjectToMap(content.get("components")).get("schemas")).get(schemaName));
    }
}