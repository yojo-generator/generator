package ru.yojo.codegen.util;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.lang.reflect.Array;
import java.util.*;

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

    public static StringBuilder getInterfaceBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_INTERFACE)
                .append(schemaName)
                .append(" {")
                .append(lineSeparator());
        return stringBuilder;
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
     * Method filling required annotations and imports
     */
    public static void buildLombokAnnotations(LombokProperties lombokProperties, Set<String> requiredImports, StringBuilder lombokAnnotationBuilder) {
        if (lombokProperties.noArgsConstructor()) {
            lombokAnnotationBuilder
                    .append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION)
                    .append(lineSeparator());
            requiredImports.add(LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT);
        }
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
        Map<String, Object> stringObjectMap = castObjectToMap(
                castObjectToMap(content.get("components"))
                        .get("schemas"));

        return castObjectToMap(stringObjectMap.get(schemaName));
    }
}