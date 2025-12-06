package ru.yojo.codegen.constants;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

/**
 * Central registry of constant string values used throughout code generation.
 * <p>
 * Contains:
 * <ul>
 *   <li>YAML keys (e.g., {@code "properties"}, {@code "$ref"})</li>
 *   <li>Java type names, package names, annotation strings, and code templates</li>
 * </ul>
 * @author Vladimir Morozkin(TG @vmorozkin)
 */
@SuppressWarnings("all")
public final class Dictionary {

    /**
     * Standard AsyncAPI/OpenAPI YAML attribute names.
     */

    /** YAML property name for object properties */
    public static final String PROPERTIES = "properties";

    /** YAML property name for data type */
    public static final String TYPE = "type";

    /** YAML property name for data format */
    public static final String FORMAT = "format";

    /** YAML property name for example value */
    public static final String EXAMPLE = "example";

    /** YAML property name for description */
    public static final String DESCRIPTION = "description";

    /** YAML property name for reference to another schema */
    public static final String REFERENCE = "$ref";

    /** YAML property name for required fields */
    public static final String REQUIRED = "required";

    /** YAML property name for maximum length constraint */
    public static final String MAX_LENGTH = "maxLength";

    /** YAML property name for minimum length constraint */
    public static final String MIN_LENGTH = "minLength";

    /** YAML property name for array items definition */
    public static final String ITEMS = "items";

    /** YAML property name for pattern constraint */
    public static final String PATTERN = "pattern";

    /** YAML property name for enumeration values */
    public static final String ENUMERATION = "enum";

    /** Custom YAML property name for enumeration names */
    public static final String X_ENUM_NAMES = "x-enumNames";

    /** YAML property name for minimum value constraint */
    public static final String MINIMUM = "minimum";

    /** YAML property name for maximum value constraint */
    public static final String MAXIMUM = "maximum";

    /** YAML data type for arrays */
    public static final String ARRAY = "array";

    /** YAML data type for objects */
    public static final String OBJECT = "object";

    /** YAML property name for schema name */
    public static final String NAME = "name";

    /** AsyncAPI property name for channels */
    public static final String CHANNELS = "channels";

    /** AsyncAPI property name for publish operations */
    public static final String PUBLISH = "publish";

    /** AsyncAPI property name for subscribe operations */
    public static final String SUBSCRIBE = "subscribe";

    /** YAML property name for title */
    public static final String TITLE = "title";

    /** YAML property name for summary */
    public static final String SUMMARY = "summary";

    /** AsyncAPI property name for message payload */
    public static final String PAYLOAD = "payload";

    /** AsyncAPI property name for message headers */
    public static final String HEADERS = "headers";

    /** YAML property name for tags */
    public static final String TAGS = "tags";

    /** YAML property name for additional properties in objects */
    public static final String ADDITIONAL_PROPERTIES = "additionalProperties";

    /** Custom YAML property name for additional format specification */
    public static final String ADDITIONAL_FORMAT = "additionalFormat";

    /** YAML property name for default value */
    public static final String DEFAULT = "default";

    /** YAML property name for allOf composition */
    public static final String ALL_OF = "allOf";

    /** YAML property name for oneOf composition */
    public static final String ONE_OF = "oneOf";

    /** YAML property name for anyOf composition */
    public static final String ANY_OF = "anyOf";

    /** YAML data type for numbers */
    public static final String NUMBER = "number";

    /** List of polymorphic composition keywords */
    public static final List<String> POLYMORPHS = List.of(ALL_OF, ONE_OF, ANY_OF);

    /**
     * Custom YAML extension attributes supported by Yojo generator.
     */

    /** Custom attribute for specifying collection implementation */
    public static final String REALIZATION = "realization";

    /** Custom attribute for validation groups */
    public static final String VALIDATION_GROUPS = "validationGroups";

    /** Custom attribute for validation groups imports */
    public static final String VALIDATION_GROUPS_IMPORTS = "validationGroupsImports";

    /** Custom attribute for enabling validation by groups */
    public static final String VALIDATE_BY_GROUPS = "validateByGroups";

    /** Custom attribute for specifying superclass */
    public static final String EXTENDS = "extends";

    /** Custom attribute for specifying implemented interfaces */
    public static final String IMPLEMENTS = "implements";

    /** Custom attribute for external class reference */
    public static final String FROM_CLASS = "fromClass";

    /** Custom attribute for external class package */
    public static final String FROM_PACKAGE = "fromPackage";

    /** Custom attribute for external interface reference */
    public static final String FROM_INTERFACE = "fromInterface";

    /** Custom attribute for package specification */
    public static final String PACKAGE = "package";

    /** Custom attribute for existing type reference */
    public static final String EXISTING = "existing";

    /** Custom attribute for interface type */
    public static final String INTERFACE = "interface";

    /** Custom attribute for method definition */
    public static final String DEFINITION = "definition";

    /** Custom attribute for schema removal flag */
    public static final String REMOVE_SCHEMA = "removeSchema";

    /** Custom attribute for digits validation */
    public static final String DIGITS = "digits";

    /** Custom attribute for multiple of validation */
    public static final String MULTIPLE_OF = "multipleOf";

    /**
     * Lombok configuration keys (manual override section).
     */

    /** Lombok configuration root key */
    public static final String LOMBOK = "lombok";

    /** Enable Lombok flag */
    public static final String ENABLE = "enable";

    /** Lombok accessors configuration */
    public static final String ACCESSORS = "accessors";

    /** Lombok fluent accessors flag */
    public static final String FLUENT = "fluent";

    /** Lombok chain accessors flag */
    public static final String CHAIN = "chain";

    /** Lombok equals and hashCode flag */
    public static final String EQUALS_AND_HASH_CODE = "equalsAndHashCode";

    /** Lombok callSuper flag for equals and hashCode */
    public static final String CALL_SUPER = "callSuper";

    /** Lombok all args constructor flag */
    public static final String ALL_ARGS = "allArgsConstructor";

    /** Lombok no args constructor flag */
    public static final String NO_ARGS = "noArgsConstructor";

    /**
     * Schema format identifiers used to map YAML types to Java.
     */

    /** Format for simple date */
    public static final String ST_SIMPLE_DATE = "simple-date";

    /** Format for local date */
    public static final String ST_LOCAL_DATE = "date";

    /** Format for local date time */
    public static final String ST_LOCAL_DATE_TIME = "local-date-time";

    /** Format for date time */
    public static final String ST_DATE_TIME = "date-time";

    /** Format for long integer */
    public static final String ST_LONG = "long";

    /** Format for integer */
    public static final String ST_INTEGER = "integer";

    /** Format for byte */
    public static final String ST_BYTE = "byte";

    /** Format for double */
    public static final String ST_DOUBLE = "double";

    /** Format for float */
    public static final String ST_FLOAT = "float";

    /** Format for UUID */
    public static final String ST_UUID = "uuid";

    /** Format for boolean */
    public static final String ST_BOOLEAN = "boolean";

    /** Format for string */
    public static final String ST_STRING = "string";

    /** Format for object */
    public static final String ST_OBJECT_TYPE = "object";

    /** Format for BigDecimal */
    public static final String ST_BIG_DECIMAL = "big-decimal";

    /** Format for BigInteger */
    public static final String ST_BIG_INTEGER = "big-integer";

    /** Format for URI */
    public static final String ST_URI = "uri";

    /**
     * Validation annotation templates (with optional placeholder formatting).
     */

    /** NotEmpty annotation template */
    public static final String NOT_EMPTY_ANNOTATION = "@NotEmpty";

    /** NotNull annotation template */
    public static final String NOT_NULL_ANNOTATION = "@NotNull";

    /** NotBlank annotation template */
    public static final String NOT_BLANK_ANNOTATION = "@NotBlank";

    /** Size annotation template */
    public static final String SIZE_ANNOTATION = "@Size";

    /** Size annotation with min and max template */
    public static final String SIZE_MIN_MAX_ANNOTATION = "@Size(min = %s, max = %s)";

    /** Size annotation with max only template */
    public static final String SIZE_MAX_ANNOTATION = "@Size(max = %s)";

    /** Size annotation with min only template */
    public static final String SIZE_MIN_ANNOTATION = "@Size(min = %s)";

    /** Pattern annotation template */
    public static final String PATTERN_ANNOTATION = "@Pattern(regexp = \"%s\")";

    /** Pattern annotation without regexp template */
    public static final String PATTERN_ANNOTATION_WITHOUT_REGEXP = "@Pattern";

    /** Valid annotation template */
    public static final String VALID_ANNOTATION = "@Valid";

    /** Digits annotation template */
    public static final String DIGITS_ANNOTATION = "@Digits(%s)";

    /** Decimal min annotation template (alias for Digits) */
    public static final String DECIMAL_MIN_ANNOTATION = "@Digits(%s)";

    /** Decimal max annotation template (alias for Digits) */
    public static final String DECIMAL_MAX_ANNOTATION = "@Digits(%s)";

    /** Minimum annotation template */
    public static final String MINIMUM_ANNOTATION = "@Min(%s)";

    /** Maximum annotation template */
    public static final String MAXIMUM_ANNOTATION = "@Max(%s)";

    /** JsonPropertyDescription annotation template */
    public static final String JSON_PROPERTY_DESCRIPTION_ANNOTATION = "@JsonPropertyDescription()";

    /**
     * Lombok annotation string templates.
     */

    /** Lombok AllArgsConstructor annotation */
    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION = "@AllArgsConstructor";

    /** Lombok NoArgsConstructor annotation */
    public static final String LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION = "@NoArgsConstructor";

    /** Lombok Data annotation */
    public static final String LOMBOK_DATA_ANNOTATION = "@Data";

    /** Lombok Getter annotation */
    public static final String LOMBOK_GETTER_ANNOTATION = "@Getter";

    /** Lombok Accessors annotation with fluent and chain */
    public static final String LOMBOK_ACCESSORS_ANNOTATION = "@Accessors(fluent = true, chain = true)";

    /** Lombok Accessors empty annotation */
    public static final String LOMBOK_ACCESSORS_EMPTY_ANNOTATION = "@Accessors";

    /** Lombok Accessors fluent annotation */
    public static final String LOMBOK_ACCESSORS_FLUENT_ANNOTATION = "@Accessors(fluent = true)";

    /** Lombok Accessors chain annotation */
    public static final String LOMBOK_ACCESSORS_CHAIN_ANNOTATION = "@Accessors(chain = true)";

    /** Lombok EqualsAndHashCode annotation */
    public static final String EQUALS_AND_HASH_CODE_ANNOTATION = "@EqualsAndHashCode";

    /** Lombok EqualsAndHashCode with callSuper=false annotation */
    public static final String EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION = "@EqualsAndHashCode(callSuper = false)";

    /** Lombok EqualsAndHashCode with callSuper=true annotation */
    public static final String EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION = "@EqualsAndHashCode(callSuper = true)";

    /**
     * Import declarations for Lombok annotations.
     */

    /** Import for Lombok AllArgsConstructor */
    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT = "lombok.AllArgsConstructor;";

    /** Import for Lombok NoArgsConstructor */
    public static final String LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT = "lombok.NoArgsConstructor;";

    /** Import for Lombok Data */
    public static final String LOMBOK_DATA_IMPORT = "lombok.Data;";

    /** Import for Lombok Getter */
    public static final String LOMBOK_GETTER_IMPORT = "lombok.Getter;";

    /** Import for Lombok Accessors */
    public static final String LOMBOK_ACCESSORS_IMPORT = "lombok.experimental.Accessors;";

    /** Import for Lombok EqualsAndHashCode */
    public static final String LOMBOK_EQUALS_AND_HASH_CODE_IMPORT = "lombok.EqualsAndHashCode;";

    /**
     * Canonical Java class simple names (used in field types, etc.).
     */

    /** Simple name for Date class */
    public static final String SIMPLE_DATE = "Date";

    /** Simple name for LocalDate class */
    public static final String LOCAL_DATE = "LocalDate";

    /** Simple name for LocalDateTime class */
    public static final String LOCAL_DATE_TIME = "LocalDateTime";

    /** Simple name for OffsetDateTime class */
    public static final String OFFSET_DATE_TIME = "OffsetDateTime";

    /** Simple name for Byte class */
    public static final String BYTE = "Byte";

    /** Simple name for Integer class */
    public static final String INTEGER = "Integer";

    /** Simple name for Long class */
    public static final String LONG = "Long";

    /** Simple name for Double class */
    public static final String DOUBLE = "Double";

    /** Simple name for Float class */
    public static final String FLOAT = "Float";

    /** Simple name for UUID class */
    public static final String UUID = "UUID";

    /** Simple name for Boolean class */
    public static final String BOOLEAN = "Boolean";

    /** Simple name for String class */
    public static final String STRING = "String";

    /** Simple name for Object class */
    public static final String OBJECT_TYPE = "Object";

    /** Simple name for BigDecimal class */
    public static final String BIG_DECIMAL = "BigDecimal";

    /** Simple name for BigInteger class */
    public static final String BIG_INTEGER = "BigInteger";

    /** Simple name for URI class */
    public static final String URI = "URI";

    /**
     * Fully qualified import declarations for Java standard types.
     */

    /** Import for BigDecimal */
    public static final String BIG_DECIMAL_IMPORT = "java.math.BigDecimal;";

    /** Import for BigInteger */
    public static final String BIG_INTEGER_IMPORT = "java.math.BigInteger;";

    /** Import for LocalDate */
    public static final String LOCAL_DATE_IMPORT = "java.time.LocalDate;";

    /** Import for LocalDateTime */
    public static final String LOCAL_DATE_TIME_IMPORT = "java.time.LocalDateTime;";

    /** Import for Date */
    public static final String SIMPLE_DATE_IMPORT = "java.util.Date;";

    /** Import for OffsetDateTime */
    public static final String OFFSET_DATE_TIME_IMPORT = "java.time.OffsetDateTime;";

    /** Import for UUID */
    public static final String UUID_IMPORT = "java.util.UUID;";

    /**
     * Import declarations for Jakarta Validation (Spring Boot 3+).
     */

    /** Jakarta NotBlank import */
    public static final String JAKARTA_NOT_BLANK_IMPORT = "jakarta.validation.constraints.NotBlank;";

    /** Jakarta NotEmpty import */
    public static final String JAKARTA_NOT_EMPTY_IMPORT = "jakarta.validation.constraints.NotEmpty;";

    /** Jakarta NotNull import */
    public static final String JAKARTA_NOT_NULL_IMPORT = "jakarta.validation.constraints.NotNull;";

    /** Jakarta Size import */
    public static final String JAKARTA_SIZE_IMPORT = "jakarta.validation.constraints.Size;";

    /** Jakarta Pattern import */
    public static final String JAKARTA_PATTERN_IMPORT = "jakarta.validation.constraints.Pattern;";

    /** Jakarta Valid import */
    public static final String JAKARTA_VALID_IMPORT = "jakarta.validation.Valid;";

    /** Jakarta Digits import */
    public static final String JAKARTA_DIGITS_IMPORT = "jakarta.validation.constraints.Digits;";

    /** Jakarta DecimalMin import */
    public static final String JAKARTA_DECIMAL_MIN_IMPORT = "jakarta.validation.constraints.DecimalMin;";

    /** Jakarta DecimalMax import */
    public static final String JAKARTA_DECIMAL_MAX_IMPORT = "jakarta.validation.constraints.DecimalMax;";

    /** Jakarta Min import */
    public static final String JAKARTA_MIN_IMPORT = "jakarta.validation.constraints.Min;";

    /** Jakarta Max import */
    public static final String JAKARTA_MAX_IMPORT = "jakarta.validation.constraints.Max;";

    /**
     * Import declarations for legacy javax.validation (Spring Boot lower 3).
     */

    /** Javax NotBlank import */
    public static final String JAVAX_NOT_BLANK_IMPORT = "javax.validation.constraints.NotBlank;";

    /** Javax NotEmpty import */
    public static final String JAVAX_NOT_EMPTY_IMPORT = "javax.validation.constraints.NotEmpty;";

    /** Javax NotNull import */
    public static final String JAVAX_NOT_NULL_IMPORT = "javax.validation.constraints.NotNull;";

    /** Javax Size import */
    public static final String JAVAX_SIZE_IMPORT = "javax.validation.constraints.Size;";

    /** Javax Pattern import */
    public static final String JAVAX_PATTERN_IMPORT = "javax.validation.constraints.Pattern;";

    /** Javax Valid import */
    public static final String JAVAX_VALID_IMPORT = "javax.validation.Valid;";

    /** Javax Digits import */
    public static final String JAVAX_DIGITS_IMPORT = "javax.validation.constraints.Digits;";

    /** Javax DecimalMin import */
    public static final String JAVAX_DECIMAL_MIN_IMPORT = "javax.validation.constraints.DecimalMin;";

    /** Javax DecimalMax import */
    public static final String JAVAX_DECIMAL_MAX_IMPORT = "javax.validation.constraints.DecimalMax;";

    /** Javax Min import */
    public static final String JAVAX_MIN_IMPORT = "javax.validation.constraints.Min;";

    /** Javax Max import */
    public static final String JAVAX_MAX_IMPORT = "javax.validation.constraints.Max;";

    /**
     * Standard collection and utility imports.
     */

    /** Import for List interface */
    public static final String LIST_IMPORT = "java.util.List;";

    /** Import for ArrayList class */
    public static final String ARRAY_LIST_IMPORT = "java.util.ArrayList;";

    /** Import for LinkedList class */
    public static final String LINKED_LIST_IMPORT = "java.util.LinkedList;";

    /** Import for Set interface */
    public static final String SET_IMPORT = "java.util.Set;";

    /** Import for HashSet class */
    public static final String HASH_SET_IMPORT = "java.util.HashSet;";

    /** Import for Map interface */
    public static final String MAP_IMPORT = "java.util.Map;";

    /** Import for HashMap class */
    public static final String HASH_MAP_IMPORT = "java.util.HashMap;";

    /** Import for LinkedHashMap class */
    public static final String LINKED_HASH_MAP_IMPORT = "java.util.LinkedHashMap;";

    /** Import for JsonPropertyDescription annotation */
    public static final String JSON_PROPERTY_DESCRIPTION_IMPORT = "com.fasterxml.jackson.annotation.JsonPropertyDescription;";

    /** Import for URI class */
    public static final String URI_IMPORT = "java.net.URI;";

    /**
     * JavaDoc comment templates (indented for field-level usage).
     */

    /** JavaDoc start line for field */
    public static final String JAVA_DOC_START = "    /**";

    /** JavaDoc end line for field */
    public static final String JAVA_DOC_END = "     */";

    /** JavaDoc content line for field */
    public static final String JAVA_DOC_LINE = "     * %s";

    /** JavaDoc example line for field */
    public static final String JAVA_DOC_EXAMPLE = "     * Example: %s";

    /** JavaDoc start for class */
    public static final String JAVA_DOC_CLASS_START = "/**";

    /** JavaDoc end for class */
    public static final String JAVA_DOC_CLASS_END = "*/";

    /** JavaDoc content line for class */
    public static final String JAVA_DOC_CLASS_LINE = "* %s";

    /**
     * Getter/Setter code templates (formatted with line separators).
     */

    /** Getter method template */
    public static final String GETTER = "    public %s get%s() {" +
                                        lineSeparator() +
                                        "        return %s;" +
                                        lineSeparator() + "    }";

    /** Setter method template */
    public static final String SETTER = "    public void set%s(%s %s) {" +
                                        lineSeparator() +
                                        "        this.%s = %s;" +
                                        lineSeparator() + "    }";

    /**
     * Enum constructor template.
     */

    /** Enum constructor template */
    public static final String ENUM_CONSTRUCTOR = "    %s(%s %s) {" +
                                                  lineSeparator() +
                                                  "    }";

    /**
     * Collection/map initialization expressions.
     */

    /** ArrayList initialization expression */
    public static final String ARRAY_LIST_REALISATION = "new ArrayList<>()";

    /** LinkedList initialization expression */
    public static final String LINKED_LIST_REALISATION = "new LinkedList<>()";

    /** HashSet initialization expression */
    public static final String HASH_SET_REALISATION = "new HashSet<>()";

    /** HashMap initialization expression */
    public static final String HASH_MAP_REALISATION = "new HashMap<>()";

    /** LinkedHashMap initialization expression */
    public static final String LINKED_HASH_MAP_REALISATION = "new LinkedHashMap<>()";

    /**
     * Generic code generation templates and constants.
     */

    /** Field declaration template */
    public static final String FIELD = "    private %s %s;";

    /** Field declaration with default value template */
    public static final String FIELD_WITH_DEFAULT_VALUE = "    private %s %s = %s;";

    /** Enum description field template */
    public static final String FIELD_ENUM_WITH_DESCRIPTION = "    private String description;";

    /** List type template */
    public static final String LIST_TYPE = "List<%s>";

    /** Set type template */
    public static final String SET_TYPE = "Set<%s>";

    /** Map type with String keys template */
    public static final String MAP_TYPE = "Map<String, %s>";

    /** Map type with custom keys template */
    public static final String MAP_CUSTOM_TYPE = "Map<%s, %s>";

    /** Enum constant template */
    public static final String ENUM_TYPE = "%s(\"%s\")";

    /** Directory delimiter */
    public static final String DELIMITER = "/";

    /** Tabulation character */
    public static final String TABULATION = "    ";

    /** Import statement prefix */
    public static final String IMPORT = "import ";

    /** Validation groups parameter */
    public static final String GROUPS = "groups = ";

    /** Messages package import */
    public static final String MESSAGE_PACKAGE_IMPORT = "messages;";

    /** Common package import */
    public static final String COMMON_PACKAGE_IMPORT = "common;";

    /** Space character */
    public static final String SPACE = " ";

    /** Public class declaration */
    public static final String PUBLIC_CLASS = "public class ";

    /** Public enum declaration */
    public static final String PUBLIC_ENUM = "public enum ";

    /** Public interface declaration */
    public static final String PUBLIC_INTERFACE = "public interface ";

    /** Methods property name */
    public static final String METHODS = "methods";

    /** Imports property name */
    public static final String IMPORTS = "imports";

    /**
     * Reserved Java keywords (used to avoid invalid field names).
     */
    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new",
            "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    );

    /**
     * Returns a safe Java field name, appending "Field" if the input is a reserved keyword.
     *
     * @param name original field name (e.g., from YAML property key)
     * @return safe Java identifier
     */
    public static String safeFieldName(String name) {
        if (JAVA_KEYWORDS.contains(name)) {
            return name + "Field";
        }
        return name;
    }

    /**
     * Maps Java field types to corresponding validation annotations for {@code required} fields.
     */
    public static final Map<String, String> JAVA_TYPES_REQUIRED_ANNOTATIONS = ofEntries(
            entry(STRING, NOT_BLANK_ANNOTATION),
            entry(BYTE, NOT_NULL_ANNOTATION),
            entry(INTEGER, NOT_NULL_ANNOTATION),
            entry(LONG, NOT_NULL_ANNOTATION),
            entry(DOUBLE, NOT_NULL_ANNOTATION),
            entry(FLOAT, NOT_NULL_ANNOTATION),
            entry(BOOLEAN, NOT_NULL_ANNOTATION),
            entry(LOCAL_DATE, NOT_NULL_ANNOTATION),
            entry(LOCAL_DATE_TIME, NOT_NULL_ANNOTATION),
            entry(SIMPLE_DATE, NOT_NULL_ANNOTATION),
            entry(BIG_DECIMAL, NOT_NULL_ANNOTATION),
            entry(BIG_INTEGER, NOT_NULL_ANNOTATION),
            entry(UUID, NOT_NULL_ANNOTATION),
            entry(OBJECT_TYPE, NOT_NULL_ANNOTATION)
    );

    /**
     * Maps annotation names to javax.validation import declarations.
     */
    public static final Map<String, String> JAVAX_JAVA_TYPES_REQUIRED_IMPORTS = ofEntries(
            entry(NOT_BLANK_ANNOTATION, JAVAX_NOT_BLANK_IMPORT),
            entry(NOT_EMPTY_ANNOTATION, JAVAX_NOT_EMPTY_IMPORT),
            entry(NOT_NULL_ANNOTATION, JAVAX_NOT_NULL_IMPORT),
            entry(SIZE_ANNOTATION, JAVAX_SIZE_IMPORT),
            entry(PATTERN_ANNOTATION_WITHOUT_REGEXP, JAVAX_PATTERN_IMPORT),
            entry(VALID_ANNOTATION, JAVAX_VALID_IMPORT)
    );

    /**
     * Maps annotation names to jakarta.validation import declarations.
     */
    public static final Map<String, String> JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS = ofEntries(
            entry(NOT_BLANK_ANNOTATION, JAKARTA_NOT_BLANK_IMPORT),
            entry(NOT_EMPTY_ANNOTATION, JAKARTA_NOT_EMPTY_IMPORT),
            entry(NOT_NULL_ANNOTATION, JAKARTA_NOT_NULL_IMPORT),
            entry(SIZE_ANNOTATION, JAKARTA_SIZE_IMPORT),
            entry(PATTERN_ANNOTATION_WITHOUT_REGEXP, JAKARTA_PATTERN_IMPORT),
            entry(VALID_ANNOTATION, JAKARTA_VALID_IMPORT)
    );

    /**
     * List of Java wrapper types generated as simple fields (not collections/maps).
     */
    public static final List<String> JAVA_DEFAULT_TYPES = List.of(
            STRING,
            BYTE,
            INTEGER,
            LONG,
            DOUBLE,
            FLOAT,
            BOOLEAN,
            BIG_DECIMAL,
            BIG_INTEGER,
            SIMPLE_DATE,
            LOCAL_DATE,
            LOCAL_DATE_TIME,
            OFFSET_DATE_TIME,
            UUID,
            URI
    );

    /**
     * Maps YAML {@code format} values (e.g., {@code "date"}) to Java type names (e.g., {@code "LocalDate"}).
     */
    public static final Map<String, Object> JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER = ofEntries(
            entry(ST_STRING, STRING),
            entry(ST_BYTE, BYTE),
            entry(ST_INTEGER, INTEGER),
            entry(ST_LONG, LONG),
            entry(ST_DOUBLE, DOUBLE),
            entry(ST_FLOAT, FLOAT),
            entry(ST_BOOLEAN, BOOLEAN),
            entry(ST_BIG_DECIMAL, BIG_DECIMAL),
            entry(ST_BIG_INTEGER, BIG_INTEGER),
            entry(ST_LOCAL_DATE, LOCAL_DATE),
            entry(ST_LOCAL_DATE_TIME, LOCAL_DATE_TIME),
            entry(ST_DATE_TIME, OFFSET_DATE_TIME),
            entry(ST_SIMPLE_DATE, SIMPLE_DATE),
            entry(ST_UUID, UUID),
            entry(ST_URI, URI)
    );

    /**
     * Reverse map: Java type names → YAML format strings.
     */
    public static final Map<String, Object> JAVA_UPPER_CASE_TYPES_CHECK_CONVERTER = ofEntries(
            entry(STRING, ST_STRING),
            entry(BYTE, ST_BYTE),
            entry(INTEGER, ST_INTEGER),
            entry(LONG, ST_LONG),
            entry(DOUBLE, ST_DOUBLE),
            entry(FLOAT, ST_FLOAT),
            entry(BOOLEAN, ST_BOOLEAN),
            entry(BIG_DECIMAL, ST_BIG_DECIMAL),
            entry(BIG_INTEGER, ST_BIG_INTEGER),
            entry(LOCAL_DATE, ST_LOCAL_DATE),
            entry(LOCAL_DATE_TIME, ST_LOCAL_DATE_TIME),
            entry(SIMPLE_DATE, ST_SIMPLE_DATE),
            entry(UUID, ST_UUID),
            entry(URI, ST_URI)
    );
}