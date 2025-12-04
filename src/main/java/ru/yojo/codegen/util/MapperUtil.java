package ru.yojo.codegen.util;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Utility class for common mapping, string, and code-generation operations.
 * <p>
 * Includes utilities for:
 * <ul>
 *   <li>YAML-to-Java type conversion and field name normalization</li>
 *   <li>Lombok annotation generation</li>
 *   <li>JavaDoc construction</li>
 *   <li>String utilities (camel/kebab/snake case, capitalization, empty checks)</li>
 *   <li>Class/interface/enum code template assembly</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class MapperUtil {

    /**
     * Safely extracts a {@code Set<String>} value from a map for the given key, or returns an empty set.
     *
     * @param key       key to lookup
     * @param schemaMap map to extract from
     * @return non-null set of strings
     */
    public static Set<String> getSetValueIfExistsOrElseEmptySet(String key, Map<String, Object> schemaMap) {
        Set<String> values = new HashSet<>();
        if (schemaMap.containsKey(key)) {
            values.addAll((ArrayList<String>) schemaMap.get(key));
        }
        return values;
    }

    /**
     * Safely extracts a string value from a map for the given key, or returns {@code null}.
     *
     * @param key map key
     * @param map source map
     * @return string value or {@code null}
     */
    public static String getStringValueIfExistOrElseNull(String key, Map<String, Object> map) {
        if (map.containsKey(key)) {
            Object value = map.getOrDefault(key, null);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }

    /**
     * Casts an object to {@code Map<String, Object>}, returning an empty map if {@code null}.
     *
     * @param obj object to cast
     * @return non-null map
     */
    public static Map<String, Object> castObjectToMap(Object obj) {
        if (obj == null) {
            return new LinkedHashMap<>();
        }
        return (Map<String, Object>) obj;
    }

    /**
     * Casts an object to {@code ArrayList<String>}, returning an empty list if {@code null}.
     *
     * @param obj object to cast
     * @return non-null list
     */
    public static ArrayList<String> castObjectToList(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }
        return (ArrayList<String>) obj;
    }

    /**
     * Casts an object to {@code ArrayList<Object>}, returning an empty list if {@code null}.
     *
     * @param obj object to cast
     * @return non-null list
     */
    public static ArrayList<Object> castObjectToListObjects(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }
        return (ArrayList<Object>) obj;
    }

    /**
     * Extracts and capitalizes the schema name from an AsyncAPI {@code $ref} path.
     * <p>
     * Examples:
     * <ul>
     *   <li>{@code "#/components/schemas/User"} → {@code "User"}</li>
     *   <li>{@code "./file.yaml#/components/schemas/user"} → {@code "User"}</li>
     * </ul>
     *
     * @param ref {@code $ref} string
     * @return capitalized schema name
     */
    public static String refReplace(String ref) {
        return capitalize(ref.replaceAll(".+/", ""));
    }

    /**
     * Generates a setter method declaration for a field.
     *
     * @param type         field type
     * @param variableName field name (camelCase)
     * @return Java setter source, e.g., {@code "    public void setField(Type field) { ... }"}
     */
    public static String generateSetter(String type, String variableName) {
        return format(SETTER, capitalize(variableName), capitalize(type), variableName, variableName, variableName);
    }

    /**
     * Generates a getter method declaration for a field.
     *
     * @param type         field type
     * @param variableName field name (camelCase)
     * @return Java getter source, e.g., {@code "    public Type getField() { ... }"}
     */
    public static String generateGetter(String type, String variableName) {
        return format(GETTER, capitalize(type), capitalize(variableName), variableName);
    }

    /**
     * Generates an enum constructor call (used internally for enum with description).
     *
     * @param enumClassName enum class name
     * @param type          enum constant type (typically {@code String})
     * @param variableName  field holding description
     * @return constructor call string
     */
    public static String generateEnumConstructor(String enumClassName, String type, String variableName) {
        return format(ENUM_CONSTRUCTOR, enumClassName, capitalize(type), variableName);
    }

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public interface X {
     * }
     * </pre>
     *
     * @param schemaName interface name
     * @return builder with interface header
     */
    public static StringBuilder getInterfaceBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_INTERFACE)
                .append(schemaName)
                .append(" {")
                .append(lineSeparator());
        return stringBuilder;
    }

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public class X {
     * }
     * </pre>
     *
     * @param schemaName class name
     * @return builder with class header
     */
    public static StringBuilder getClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_CLASS)
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public enum X {
     * }
     * </pre>
     *
     * @param schemaName enum name
     * @return builder with enum header
     */
    public static StringBuilder getEnumClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(PUBLIC_ENUM)
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public class X implements I1, I2 {
     * }
     * </pre>
     *
     * @param schemaName class name
     * @param implementsFrom set of interface simple names
     * @return builder with class+implements header
     */
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

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public class X extends Y {
     * }
     * </pre>
     *
     * @param schemaName class name
     * @param extendsClass superclass name
     * @return builder with class+extends header
     */
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

    /**
     * Creates a {@code StringBuilder} initialized with:
     * <pre>
     * public class X extends Y implements I1, I2 {
     * }
     * </pre>
     *
     * @param schemaName class name
     * @param extendsClass superclass name
     * @param implementsFrom set of interface names
     * @return builder with full inheritance header
     */
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
     * Appends a field-level JavaDoc comment (with optional example) to the given {@link StringBuilder}.
     *
     * @param sb          builder to append to
     * @param description field description (from {@code description} in schema)
     * @param example     field example (from {@code example} in schema)
     */
    public static void generateJavaDoc(StringBuilder sb, String description, String example) {
        if (isNoneEmpty(description) || isNoneEmpty(example)) {
            sb.append(lineSeparator()).append(JAVA_DOC_START);
            if (isNotBlank(description)) {
                sb.append(lineSeparator()).append(format(JAVA_DOC_LINE, description));
            }
            if (isNotBlank(example)) {
                sb.append(lineSeparator()).append(format(JAVA_DOC_EXAMPLE, example));
            }
            sb.append(lineSeparator()).append(JAVA_DOC_END);
        }
    }

    /**
     * Prepends a class-level JavaDoc comment to the given {@link StringBuilder}.
     * Inserted before {@code public class X { ...}}.
     *
     * @param sb          builder to modify (via {@code insert(0, ...)})
     * @param description class description (from {@code description} in schema)
     */
    public static void generateClassJavaDoc(StringBuilder sb, String description) {
        if (isNoneEmpty(description)) {
            sb.insert(0, lineSeparator());
            sb.insert(0, JAVA_DOC_CLASS_END);
            sb.insert(0, lineSeparator());
            sb.insert(0, format(JAVA_DOC_CLASS_LINE, description));
            sb.insert(0, lineSeparator());
            sb.insert(0, JAVA_DOC_CLASS_START);
        }
    }

    /**
     * Joins package parts with dots.
     *
     * @param packageLocation      base package (e.g., {@code com.example})
     * @param outputDirectoryName  subdirectory name (e.g., {@code test})
     * @param messagePackageImport suffix (e.g., {@code messages;})
     * @return full package string (e.g., {@code com.example.test.messages;})
     */
    public static String getPackage(String packageLocation, String outputDirectoryName, String messagePackageImport) {
        return String.join(".", packageLocation, outputDirectoryName, messagePackageImport);
    }

    /**
     * Generates {@code @Size} annotation based on min/max bounds.
     *
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return annotation string
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

    /**
     * Generates {@code @Min(...)} annotation.
     *
     * @param min minimum value
     * @return annotation string
     */
    public static String generateMinAnnotation(String min) {
        return format(MINIMUM_ANNOTATION, min);
    }

    /**
     * Generates {@code @Max(...)} annotation.
     *
     * @param max maximum value
     * @return annotation string
     */
    public static String generateMaxAnnotation(String max) {
        return format(MAXIMUM_ANNOTATION, max);
    }

    /**
     * Appends Lombok annotations (e.g., {@code @NoArgsConstructor}, {@code @Accessors}) to the builder
     * and adds corresponding imports to the set.
     *
     * @param lombokProperties        Lombok config
     * @param requiredImports         target import set
     * @param lombokAnnotationBuilder target builder for annotations
     */
    public static void buildLombokAnnotations(LombokProperties lombokProperties,
                                              Set<String> requiredImports,
                                              StringBuilder lombokAnnotationBuilder) {
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
            if (Boolean.TRUE.equals(lombokProperties.getEqualsAndHashCode().getCallSuper())) {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION)
                        .append(lineSeparator());
            } else if (Boolean.FALSE.equals(lombokProperties.getEqualsAndHashCode().getCallSuper())) {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION)
                        .append(lineSeparator());
            } else {
                lombokAnnotationBuilder.append(EQUALS_AND_HASH_CODE_ANNOTATION)
                        .append(lineSeparator());
            }
        }
    }

    /**
     * Selects the appropriate {@code @Accessors(...)} annotation based on fluent/chain flags.
     *
     * @param lombokProperties Lombok config
     * @return annotation string
     */
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

    /**
     * Finalizes Java source by prepending package and imports, and appending closing brace.
     *
     * @param sb              builder with class body
     * @param requiredImports set of import strings
     * @param packageName     package name (with semicolon)
     * @return complete Java source
     */
    public static String finishBuild(StringBuilder sb, Set<String> requiredImports, String packageName) {
        StringBuilder importBuilder = new StringBuilder();
        requiredImports.forEach(imp -> {
            importBuilder
                    .append(IMPORT)
                    .append(imp)
                    .append(lineSeparator());
        });
        sb.insert(0, importBuilder.append(lineSeparator()));

        sb.insert(0, new StringBuilder("package ")
                .append(packageName)
                .append(lineSeparator())
                .append(lineSeparator()));

        return sb
                .append(lineSeparator())
                .append("}")
                .toString();
    }

    /**
     * Prepares a class builder with correct inheritance/implementation clause and appends field declarations.
     *
     * @param requiredImports accumulated imports (updated in-place)
     * @param implementsFrom  interfaces to implement
     * @param extendsFrom     superclass to extend
     * @param schemaName      class name
     * @param importSet       extra imports (e.g., from {@code extends}/{@code implements})
     * @param fillParameters  field container
     * @return initialized builder with fields appended
     */
    public static StringBuilder prepareStringBuilder(Set<String> requiredImports,
                                                     Set<String> implementsFrom,
                                                     String extendsFrom,
                                                     String schemaName,
                                                     Set<String> importSet,
                                                     FillParameters fillParameters) {
        StringBuilder sb;
        if (!implementsFrom.isEmpty() && isNotBlank(extendsFrom)) {
            sb = getExtendsWithImplementationClassBuilder(schemaName, extendsFrom, implementsFrom);
        } else if (!implementsFrom.isEmpty()) {
            sb = getImplementationClassBuilder(schemaName, implementsFrom);
        } else if (isNotBlank(extendsFrom)) {
            sb = getExtendsClassBuilder(schemaName, extendsFrom);
        } else {
            sb = getClassBuilder(schemaName);
        }

        requiredImports.addAll(importSet);
        sb.append(fillParameters.toWrite()).append(lineSeparator());
        return sb;
    }

    /*
     * String utilities (reimplementation of Apache Commons Lang for zero-dependency)
     */

    /**
     * Converts the first character to title case (e.g., {@code "userName"} → {@code "UserName"}).
     *
     * @param str input string
     * @return capitalized string
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

    /**
     * Converts the first character to lower case (e.g., {@code "UserName"} → {@code "userName"}).
     *
     * @param str input string
     * @return uncapitalized string
     */
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
            return str;
        }

        final int[] newCodePoints = strLen;
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen1; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Checks if a {@code CharSequence} is null, empty, or whitespace only.
     *
     * @param cs char sequence
     * @return {@code true} if blank
     */
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

    /**
     * Checks if a {@code CharSequence} is not blank.
     *
     * @param cs char sequence
     * @return {@code true} if non-blank
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * Checks if any of the input strings is null or empty.
     *
     * @param css vararg strings
     * @return {@code true} if at least one is empty
     */
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

    /**
     * Checks if none of the input strings is null or empty.
     *
     * @param css vararg strings
     * @return {@code true} if all are non-empty
     */
    public static boolean isNoneEmpty(final CharSequence... css) {
        return !isAnyEmpty(css);
    }

    /**
     * Checks if a {@code CharSequence} is {@code null} or zero-length.
     *
     * @param cs char sequence
     * @return {@code true} if empty
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Returns length of a {@code CharSequence}, or 0 if {@code null}.
     *
     * @param cs char sequence
     * @return length
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * Checks if an object array is empty.
     *
     * @param array array
     * @return {@code true} if empty or {@code null}
     */
    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    /**
     * Returns length of an array or {@code 0} if {@code null}.
     *
     * @param array array
     * @return length
     */
    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    /**
     * Gets substring before the first occurrence of a separator.
     *
     * @param str       source string
     * @param separator separator
     * @return substring before separator, or original if not found
     */
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

    /**
     * Checks if a {@code Boolean} is {@code true}.
     *
     * @param bool boolean wrapper
     * @return {@code true} if non-null and {@code true}
     */
    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * Retrieves schema definition by name from the global {@code components.schemas} section.
     *
     * @param content    full AsyncAPI document
     * @param schemaName schema name
     * @return schema map or empty map
     */
    public static Map<String, Object> getSchemaByName(Map<String, Object> content, String schemaName) {
        Map<String, Object> schemas = castObjectToMap(
                castObjectToMap(content.get("components"))
                        .get("schemas"));
        return castObjectToMap(schemas.get(schemaName));
    }

    /**
     * Converts a raw field name (e.g., {@code "user-name", "user_name"}) to valid Java field identifier (camelCase).
     * Respects keyword safety.
     *
     * @param rawName raw name from YAML
     * @return safe camelCase name
     */
    public static String toValidJavaFieldName(String rawName) {
        if (rawName == null || rawName.isEmpty()) {
            return rawName;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < rawName.length(); i++) {
            char c = rawName.charAt(i);
            if (c == '-' || c == '_' || c == ' ') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return safeFieldName(sb.toString());
    }

    /**
     * Converts raw enum constant name (e.g., {@code "success", "error-code", "\r\n"})
     * to valid Java enum identifier (UPPER_SNAKE_CASE), handling control chars and keywords.
     *
     * @param rawName raw enum name
     * @return valid UPPER_SNAKE_CASE enum constant name
     */
    public static String toValidJavaEnumConstantName(String rawName) {
        if (rawName == null || rawName.isEmpty()) {
            return "UNKNOWN";
        }

        // Handle special whitespace/control chars
        if ("\r\n".equals(rawName)) {
            return "CARRIAGE_RETURN_LINE_FEED";
        }
        if ("\n".equals(rawName)) {
            return "LINE_FEED";
        }
        if ("\r".equals(rawName)) {
            return "CARRIAGE_RETURN";
        }
        if (" ".equals(rawName)) {
            return "SPACE";
        }
        if (rawName.matches("[\\x00-\\x1F\\x7F]")) {
            return "CTRL_" + Integer.toHexString(rawName.charAt(0)).toUpperCase();
        }

        // Clean and convert to UPPER_SNAKE_CASE
        String cleaned = rawName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        StringBuilder sb = new StringBuilder();
        boolean prevWasLower = false;
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c == '-' || c == '_') {
                sb.append('_');
                prevWasLower = false;
                continue;
            }
            if (Character.isUpperCase(c) && prevWasLower && sb.length() > 0 && sb.charAt(sb.length() - 1) != '_') {
                sb.append('_');
            }
            sb.append(Character.toUpperCase(c));
            prevWasLower = Character.isLowerCase(c);
        }

        String result = sb.toString()
                .replaceAll("__+", "_")
                .replaceAll("^_+|_+$", "");
        if (result.isEmpty()) result = "VALUE";

        return safeFieldName(result);
    }
}