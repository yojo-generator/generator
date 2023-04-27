package ru.yojo.codegen.util;

import ru.yojo.codegen.constants.ConstantsEnum;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.exception.SchemaFillException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;

@SuppressWarnings("all")
public class MapperUtil {

    public static Set<String> getSetValueIfExistsOrElseEmptySet(String value, Map<String, Object> schemaMap) {
        Set<String> values = new HashSet<>();
        if (schemaMap.containsKey(value)) {
            values.addAll((ArrayList<String>) schemaMap.get(value));
        }
        return values;
    }

    public static String getStringValueIfExistOrElseNull(ConstantsEnum valueEnum, Map<String, Object> map) {
        if (map.containsKey(valueEnum.getValue())) {
            Object value = map.getOrDefault(valueEnum.getValue(), null);
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
            return new ConcurrentHashMap<>();
        }
        return (Map<String, Object>) map;
    }

    public static ArrayList<String> castObjectToList(Object list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) list;
    }

    public static String refReplace(String ref) {
        return capitalize(ref.replaceAll(".+/", ""));
    }

    public static String generateSetter(String type, String variableName) {
        return String.format(SETTER.getValue(), capitalize(variableName), capitalize(type), variableName, variableName, variableName);
    }

    public static String generateGetter(String type, String variableName) {
        return String.format(GETTER.getValue(), capitalize(type), capitalize(variableName), variableName);
    }

    public static void generateValidAnnotation(StringBuilder stringBuilder) {
        stringBuilder.append(lineSeparator())
                .append(VALID_ANNOTATION.getValue());
    }

    public static StringBuilder getClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getEnumClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public enum ")
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getImplementationClassBuilder(String schemaName, String implementationClass) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" implements ")
                .append(implementationClass)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getExtendsClassBuilder(String schemaName, String extendsClass) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" " + extendsClass)
                .append(" {");
        return stringBuilder;
    }

    public static StringBuilder getExtendsWithImplementationClassBuilder(String schemaName, String extendsClass, String implementationClass) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" " + extendsClass)
                .append(" implements ")
                .append(implementationClass)
                .append(" {");
        return stringBuilder;
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
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_START.getValue());
            if (isNotBlank(description)) {
                stringBuilder.append(lineSeparator()).append(formatString(JAVA_DOC_LINE, description));
            }
            if (isNotBlank(example)) {
                stringBuilder.append(lineSeparator()).append(formatString(JAVA_DOC_EXAMPLE, example));
            }
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_END.getValue());
        }
    }

    public static String getPackage(String packageLocation, String outputDirectoryName, ConstantsEnum messagePackageImport) {
        return String.join(".", packageLocation, outputDirectoryName, messagePackageImport.getValue());
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
            return formatString(SIZE_MIN_MAX_ANNOTATION, minLength, maxLength);
        } else if (isNotBlank(minLength)) {
            return formatString(SIZE_MIN_ANNOTATION, minLength);
        } else {
            return formatString(SIZE_MAX_ANNOTATION, maxLength);
        }
    }

    /**
     * Fill properties
     *
     * @param variableProperties variableProperties
     * @param requiredAttributes requiredAttributes
     * @param schemas            schemas
     * @param propertyName       propertyName
     * @param propertiesMap      propertiesMap
     * @param commonPackage      commonPackage
     * @param innerSchemas
     */
    public static void fillProperties(VariableProperties variableProperties, Set<String> requiredAttributes, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        variableProperties.setName(propertyName);
        variableProperties.setType(capitalize(getStringValueIfExistOrElseNull(TYPE, propertiesMap)));
        variableProperties.setDescription(getStringValueIfExistOrElseNull(DESCRIPTION, propertiesMap));
        variableProperties.setExample(getStringValueIfExistOrElseNull(EXAMPLE, propertiesMap));
        variableProperties.setTitle(getStringValueIfExistOrElseNull(TITLE, propertiesMap));
        variableProperties.setFormat(getStringValueIfExistOrElseNull(FORMAT, propertiesMap));
        variableProperties.setPattern(getStringValueIfExistOrElseNull(PATTERN, propertiesMap));
        variableProperties.setMinMaxLength(getStringValueIfExistOrElseNull(MIN_LENGTH, propertiesMap), getStringValueIfExistOrElseNull(MAX_LENGTH, propertiesMap));
        variableProperties.setEnumeration(getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap));
        variableProperties.setEnumNames(getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap));

        recursivelyFillProperties(variableProperties, requiredAttributes, schemas, propertyName, propertiesMap, commonPackage, innerSchemas);
        Set<String> annotationSet = new HashSet<>();
        Set<String> importSet = new HashSet<>();
        fillRequiredAnnotationsAndImports(variableProperties, requiredAttributes, propertyName, annotationSet, importSet);
    }

    /**
     * Recursive Filling from $ref Objects
     *
     * @param variableProperties variableProperties
     * @param requiredAttributes requiredAttributes
     * @param schemas            schemas
     * @param propertyName       propertyName
     * @param propertiesMap      propertiesMap
     * @param commonPackage      commonPackage
     * @param innerSchemas
     */
    public static void recursivelyFillProperties(VariableProperties variableProperties, Set<String> requiredAttributes, Map<String, Object> schemas, String propertyName, Map<String, Object> propertiesMap, String commonPackage, Map<String, Object> innerSchemas) {
        if (ARRAY.getValue().equals(uncapitalize(variableProperties.getType()))) {
            if (getStringValueIfExistOrElseNull(REFERENCE, castObjectToMap(propertiesMap.get(ITEMS.getValue()))) != null) {
                variableProperties.setItems(refReplace(propertiesMap.get(ITEMS.getValue()).toString().replaceFirst(".$", "")));
                variableProperties.setType(String.format(refReplace(LIST_TYPE.getValue()), variableProperties.getItems()));
            } else {
                Map<String, Object> stringObjectMap = castObjectToMap(propertiesMap.get(ITEMS.getValue()));
                variableProperties.setItems(stringObjectMap.get(FORMAT.getValue()).toString());
                variableProperties.setFormat(stringObjectMap.get(FORMAT.getValue()).toString());
            }
            if (!JAVA_DEFAULT_TYPES.contains(variableProperties.getItems()) && !OBJECT_TYPE.getValue().equals(variableProperties.getItems())) {
                variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + variableProperties.getItems() + ";"));
            }
        } else if (getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null && !ARRAY.getValue().equals(uncapitalize(variableProperties.getType()))) {
            String referenceObject = propertiesMap.get(REFERENCE.getValue()).toString();
            Map<String, Object> stringObjectMap = castObjectToMap(schemas.get(referenceObject.replaceAll(".+/", "")));
            String objectType = getStringValueIfExistOrElseNull(TYPE, stringObjectMap);
            if (objectType != null && JAVA_DEFAULT_SCHEMA_TYPES.containsKey(objectType)) {
                //Recursive Fill
                System.out.println();
                System.out.println("Start Recursive fillProperties " + propertyName);
                System.out.println();
                fillProperties(variableProperties, requiredAttributes, schemas, propertyName, stringObjectMap, commonPackage, innerSchemas);
            }
            if (variableProperties.getType() == null || OBJECT_TYPE.getValue().equals(variableProperties.getType())) {
                String refReplace = refReplace(referenceObject);
                variableProperties.setType(refReplace);
                if (Character.isUpperCase(refReplace.charAt(0))) {
                    variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + refReplace + ";"));
                }
            }
        } else if (OBJECT_TYPE.getValue().equals(variableProperties.getType()) && getStringValueIfExistOrElseNull(PROPERTIES, propertiesMap) != null) {
            System.out.println("FOUND INNER SCHEMA!!! " + propertyName);
            variableProperties.setType(capitalize(propertyName));
            variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + capitalize(propertyName) + ";"));
            innerSchemas.put(propertyName, propertiesMap);
        } else if (OBJECT_TYPE.getValue().equals(variableProperties.getType()) && getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            System.out.println("ENUMERATION FOUND");
            fillEnumSchema(propertyName, propertiesMap, innerSchemas);
            variableProperties.setType(capitalize(propertyName));
            variableProperties.setEnumNames(null);
            variableProperties.setEnumeration(null);
            variableProperties.setEnum(true);
            variableProperties.getRequiredImports().add(commonPackage.replace(";", "." + capitalize(propertyName) + ";"));
        } else if (getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null) {
            System.out.println();
            System.out.println("ADDITIONAL PROPERTIES");
            String type = getStringValueIfExistOrElseNull(TYPE, castObjectToMap(propertiesMap.get(ADDITIONAL_PROPERTIES.getValue())));
            if (JAVA_DEFAULT_SCHEMA_TYPES.containsKey(type)) {
                System.out.println("CORRECT TYPE!");
                variableProperties.setType(String.format(MAP_TYPE.getValue(), JAVA_DEFAULT_SCHEMA_TYPES.get(type)));
            } else if (OBJECT.getValue().equals(type)) {
                variableProperties.setType(String.format(MAP_TYPE.getValue(), OBJECT_TYPE.getValue()));
            }
            System.out.println();
        }
    }

    private static void fillEnumSchema(String propertyName, Map<String, Object> propertiesMap, Map<String, Object> innerSchemas) {
        if (getStringValueIfExistOrElseNull(X_ENUM_NAMES, propertiesMap) != null) {
            Map<String, Object> enumerationMap = castObjectToMap(propertiesMap.get(X_ENUM_NAMES.getValue()));
            Map<String, Object> enums = new HashMap<>();
            Map<String, Object> enumWithDescription = fillByEnumWithDescription(enumerationMap);
            innerSchemas.put(propertyName, enumWithDescription);
        } else if (getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null) {
            List<String> enums = castObjectToList(propertiesMap.get(ENUMERATION.getValue()));
            Map<String, Object> enumsMap = fillByEnum(enums);
            innerSchemas.put(propertyName, enumsMap);
        }
    }

    private static Map<String, Object> fillByEnumWithDescription(Map<String, Object> enums) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        Map<String, Object> vp = new ConcurrentHashMap<>();
        Map<String, Object> type = new ConcurrentHashMap<>();
        Map<String, Object> properties = new ConcurrentHashMap<>(){};
        type.put(TYPE.getValue(), OBJECT.getValue());
        enums.forEach((enumName, enumDescription) -> {
            Map<String, Object> prop = new ConcurrentHashMap<>();
            prop.put(TYPE.getValue(), formatString(ENUM_TYPE, enumName, enumDescription));
            prop.put(X_ENUM_NAMES.getValue(), enumDescription);
            prop.put(ENUMERATION.getValue(), enumName);
            vp.put(enumName, prop);
        });
        properties.put(PROPERTIES.getValue(), vp);
        result.putAll(type);
        result.putAll(properties);
        return result;
    }

    private static Map<String, Object> fillByEnum(List<String> enums) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        Map<String, Object> vp = new ConcurrentHashMap<>();
        Map<String, Object> type = new ConcurrentHashMap<>();
        Map<String, Object> properties = new ConcurrentHashMap<>(){};
        type.put(TYPE.getValue(), OBJECT.getValue());
        enums.forEach(enumName -> {
            Map<String, Object> prop = new ConcurrentHashMap<>();
            prop.put(TYPE.getValue(), enumName);
            prop.put(ENUMERATION.getValue(), enumName);
            vp.put(enumName, prop);
        });
        properties.put(PROPERTIES.getValue(), vp);
        result.putAll(type);
        result.putAll(properties);
        return result;
    }

    /**
     * Method filling required annotations and imports
     *
     * @param variableProperties variableProperties
     * @param requiredAttributes requiredAttributes
     * @param propertyName       propertyName
     * @param annotationSet      annotationSet
     * @param importSet          importSet
     */
    public static void fillRequiredAnnotationsAndImports(VariableProperties variableProperties, Set<String> requiredAttributes, String propertyName, Set<String> annotationSet, Set<String> importSet) {
        requiredAttributes.forEach(requiredAttribute -> {
            if (requiredAttribute.contains(propertyName)) {
                if (variableProperties.getItems() != null) {
                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(NOT_EMPTY_ANNOTATION.getValue()));
                    annotationSet.add(NOT_EMPTY_ANNOTATION.getValue());
                } else {
                    if (isBlank(variableProperties.getType())) {
                        throw new SchemaFillException("TYPE not found: " + propertyName);
                    }
                    String annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(variableProperties.getType());

                    if (isBlank(annotation)) {
                        annotation = JAVA_TYPES_REQUIRED_ANNOTATIONS.get(OBJECT_TYPE.getValue());
                    }

                    if (isBlank(annotation)) {
                        throw new SchemaFillException("Incorrect filling attribute: " + propertyName);
                    }

                    importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(annotation));
                    annotationSet.add(annotation);
                }
            }
            if (variableProperties.isEnum() == false && variableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(variableProperties.getType())) {
                importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION.getValue()));
                annotationSet.add(VALID_ANNOTATION.getValue());
            }
        });
        if (variableProperties.isEnum() == false && variableProperties.getType() != null && !JAVA_DEFAULT_TYPES.contains(variableProperties.getType())) {
            importSet.add(JAVA_TYPES_REQUIRED_IMPORTS.get(VALID_ANNOTATION.getValue()));
            annotationSet.add(VALID_ANNOTATION.getValue());
        }
        variableProperties.getRequiredImports().addAll(importSet);
        variableProperties.getAnnotationSet().addAll(annotationSet);
    }

    public static void buildLombokAnnotations(LombokProperties lombokProperties, Set<String> requiredImports, StringBuilder lombokAnnotationBuilder) {
        lombokAnnotationBuilder
                .append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION.getValue())
                .append(lineSeparator());
        requiredImports.add(LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT.getValue());

        if (lombokProperties.getAccessors().isEnable()) {
            String accessors = null;
            if (lombokProperties.getAccessors().isChain()) {
                accessors = LOMBOK_ACCESSORS_CHAIN_ANNOTATION.getValue();
            } else if (lombokProperties.getAccessors().isFluent()) {
                accessors = LOMBOK_ACCESSORS_FLUENT_ANNOTATION.getValue();
            } else if (lombokProperties.getAccessors().isChain() && lombokProperties.getAccessors().isFluent()) {
                accessors = LOMBOK_ACCESSORS_ANNOTATION.getValue();
            } else {
                accessors = LOMBOK_ACCESSORS_EMPTY_ANNOTATION.getValue();
            }
            lombokAnnotationBuilder.append(accessors)
                    .append(lineSeparator());
            requiredImports.add(LOMBOK_ACCESSORS_IMPORT.getValue());
        }
        if (lombokProperties.allArgsConstructor()) {
            lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION.getValue())
                    .append(lineSeparator());
            requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT.getValue());
        }
    }


    public static String finishBuild(StringBuilder stringBuilder, Set<String> requiredImports, String packageName) {
        StringBuilder importBuilder = new StringBuilder();
        requiredImports.forEach(requiredImport -> {
            importBuilder
                    .append(IMPORT.getValue())
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
}