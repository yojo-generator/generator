package ru.yojo.codegen.constants;

import java.util.List;
import java.util.Map;

import static java.lang.System.lineSeparator;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@SuppressWarnings("all")
public final class Dictionary {

    /**
     * Attributes Yaml
     */
    public static final String PROPERTIES = "properties";
    public static final String TYPE = "type";
    public static final String FORMAT = "format";
    public static final String EXAMPLE = "example";
    public static final String DESCRIPTION = "description";
    public static final String REFERENCE = "$ref";
    public static final String REQUIRED = "required";
    public static final String MAX_LENGTH = "maxLength";
    public static final String MIN_LENGTH = "minLength";
    public static final String ITEMS = "items";
    public static final String PATTERN = "pattern";
    public static final String ENUMERATION = "enum";
    public static final String X_ENUM_NAMES = "x-enumNames";
    public static final String DIGITS = "digits";
    public static final String MINIMUM = "minimum";
    public static final String MAXIMUM = "maximum";
    public static final String ARRAY = "array";
    public static final String OBJECT = "object";
    public static final String NAME = "name";
    public static final String CHANNELS = "channels";
    public static final String PUBLISH = "publish";
    public static final String SUBSCRIBE = "subscribe";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String PAYLOAD = "payload";
    public static final String HEADERS = "headers";
    public static final String TAGS = "tags";
    public static final String ADDITIONAL_PROPERTIES = "additionalProperties";
    public static final String ADDITIONAL_FORMAT = "additionalFormat";
    public static final String DEFAULT = "default";
    public static final String ALL_OF = "allOf";
    public static final String ONE_OF = "oneOf";
    public static final String ANY_OF = "anyOf";
    public static final List<String> POLYMORPHS = List.of(ALL_OF, ONE_OF, ANY_OF);

    /**
     * Custom YAML attributes
     */
    public static final String REALIZATION = "realization";
    public static final String PRIMITIVE = "primitive";
    public static final String VALIDATION_GROUPS = "validationGroups";
    public static final String VALIDATION_GROUPS_IMPORTS = "validationGroupsImports";
    public static final String VALIDATE_BY_GROUPS = "validateByGroups";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String FROM_CLASS = "fromClass";
    public static final String FROM_PACKAGE = "fromPackage";
    public static final String FROM_INTERFACE = "fromInterface";
    public static final String SET = "Set";
    public static final String PACKAGE = "package";
    public static final String EXISTING = "existing";

    /**
     * lombok manual properties
     */
    public static final String LOMBOK = "lombok";
    public static final String ENABLE = "enable";

    public static final String ACCESSORS = "accessors";
    public static final String FLUENT = "fluent";
    public static final String CHAIN = "chain";

    public static final String EQUALS_AND_HASH_CODE = "equalsAndHashCode";
    public static final String CALL_SUPER = "callSuper";

    /**
     * Schema Types
     */
    public static final String ST_SIMPLE_DATE = "simple-date";
    public static final String ST_LOCAL_DATE = "date";
    public static final String ST_LOCAL_DATE_TIME = "date-time";
    public static final String ST_LONG = "long";
    public static final String ST_INTEGER = "integer";
    public static final String ST_BYTE = "byte";
    public static final String ST_DOUBLE = "double";
    public static final String ST_FLOAT = "float";
    public static final String ST_UUID = "uuid";
    public static final String ST_BOOLEAN = "boolean";
    public static final String ST_STRING = "string";
    public static final String ST_OBJECT_TYPE = "object";
    public static final String ST_BIG_DECIMAL = "bigDecimal";
    public static final String ST_BIG_INTEGER = "bigInteger";

    /**
     * Annotations
     */
    public static final String NOT_EMPTY_ANNOTATION = "@NotEmpty";
    public static final String NOT_NULL_ANNOTATION = "@NotNull";
    public static final String NOT_BLANK_ANNOTATION = "@NotBlank";
    public static final String SIZE_ANNOTATION = "@Size";
    public static final String SIZE_MIN_MAX_ANNOTATION = "@Size(min = %s, max = %s)";
    public static final String SIZE_MAX_ANNOTATION = "@Size(max = %s)";
    public static final String SIZE_MIN_ANNOTATION = "@Size(min = %s)";
    public static final String PATTERN_ANNOTATION = "@Pattern(regexp = \"%s\")";
    public static final String PATTERN_ANNOTATION_WITHOUT_REGEXP = "@Pattern";
    public static final String VALID_ANNOTATION = "@Valid";
    public static final String DIGITS_ANNOTATION = "@Digits(%s)";
    public static final String MINIMUM_ANNOTATION = "@Min(%s)";
    public static final String MAXIMUM_ANNOTATION = "@Max(%s)";

    /**
     * Lombok annotations
     */
    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION = "@AllArgsConstructor";
    public static final String LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION = "@NoArgsConstructor";
    public static final String LOMBOK_DATA_ANNOTATION = "@Data";
    public static final String LOMBOK_GETTER_ANNOTATION = "@Getter";
    public static final String LOMBOK_ACCESSORS_ANNOTATION = "@Accessors(fluent = true, chain = true)";
    public static final String LOMBOK_ACCESSORS_EMPTY_ANNOTATION = "@Accessors";
    public static final String LOMBOK_ACCESSORS_FLUENT_ANNOTATION = "@Accessors(fluent = true)";
    public static final String LOMBOK_ACCESSORS_CHAIN_ANNOTATION = "@Accessors(chain = true)";
    public static final String EQUALS_AND_HASH_CODE_ANNOTATION = "@EqualsAndHashCode";
    public static final String EQUALS_AND_HASH_CODE_CALL_SUPER_FALSE_ANNOTATION = "@EqualsAndHashCode(callSuper = false)";
    public static final String EQUALS_AND_HASH_CODE_CALL_SUPER_TRUE_ANNOTATION = "@EqualsAndHashCode(callSuper = true)";

    /**
     * Lombok Imports
     */
    public static final String LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT = "lombok.AllArgsConstructor;";
    public static final String LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT = "lombok.NoArgsConstructor;";
    public static final String LOMBOK_DATA_IMPORT = "lombok.Data;";
    public static final String LOMBOK_GETTER_IMPORT = "lombok.Getter;";
    public static final String LOMBOK_ACCESSORS_IMPORT = "lombok.experimental.Accessors;";
    public static final String LOMBOK_EQUALS_AND_HASH_CODE_IMPORT = "lombok.EqualsAndHashCode;";

    /**
     * Java Types
     */
    public static final String SIMPLE_DATE = "Date";
    public static final String LOCAL_DATE = "LocalDate";
    public static final String LOCAL_DATE_TIME = "LocalDateTime";
    public static final String OFFSET_DATE_TIME = "OffsetDateTime";
    public static final String BYTE = "Byte";
    public static final String INTEGER = "Integer";
    public static final String LONG = "Long";
    public static final String DOUBLE = "Double";
    public static final String FLOAT = "Float";
    public static final String UUID = "UUID";
    public static final String BOOLEAN = "Boolean";
    public static final String STRING = "String";
    public static final String OBJECT_TYPE = "Object";
    public static final String BIG_DECIMAL = "BigDecimal";
    public static final String BIG_INTEGER = "BigInteger";

    /**
     * Packages
     */
    public static final String BIG_DECIMAL_IMPORT = "java.math.BigDecimal;";
    public static final String BIG_INTEGER_IMPORT = "java.math.BigInteger;";
    public static final String LOCAL_DATE_IMPORT = "java.time.LocalDate;";
    public static final String LOCAL_DATE_TIME_IMPORT = "java.time.LocalDateTime;";
    public static final String SIMPLE_DATE_IMPORT = "java.util.Date;";
    public static final String OFFSET_DATE_TIME_IMPORT = "java.time.OffsetDateTime;";
    public static final String UUID_IMPORT = "java.util.UUID;";


    /**
     * JAVAX
     */
    public static final String JAVAX_NOT_BLANK_IMPORT = "javax.validation.constraints.NotBlank;";
    public static final String JAVAX_NOT_EMPTY_IMPORT = "javax.validation.constraints.NotEmpty;";
    public static final String JAVAX_NOT_NULL_IMPORT = "javax.validation.constraints.NotNull;";
    public static final String JAVAX_SIZE_IMPORT = "javax.validation.constraints.Size;";
    public static final String JAVAX_PATTERN_IMPORT = "javax.validation.constraints.Pattern;";
    public static final String JAVAX_VALID_IMPORT = "javax.validation.Valid;";
    public static final String JAVAX_DIGITS_IMPORT = "javax.validation.constraints.Digits;";
    public static final String JAVAX_MIN_IMPORT = "javax.validation.constraints.Min;";
    public static final String JAVAX_MAX_IMPORT = "javax.validation.constraints.Max;";

    /**
     * JAKARTA
     */
    public static final String JAKARTA_NOT_BLANK_IMPORT = "jakarta.validation.constraints.NotBlank;";
    public static final String JAKARTA_NOT_EMPTY_IMPORT = "jakarta.validation.constraints.NotEmpty;";
    public static final String JAKARTA_NOT_NULL_IMPORT = "jakarta.validation.constraints.NotNull;";
    public static final String JAKARTA_SIZE_IMPORT = "jakarta.validation.constraints.Size;";
    public static final String JAKARTA_PATTERN_IMPORT = "jakarta.validation.constraints.Pattern;";
    public static final String JAKARTA_VALID_IMPORT = "jakarta.validation.Valid;";
    public static final String JAKARTA_DIGITS_IMPORT = "jakarta.validation.constraints.Digits;";
    public static final String JAKARTA_MIN_IMPORT = "jakarta.validation.constraints.Min;";
    public static final String JAKARTA_MAX_IMPORT = "jakarta.validation.constraints.Max;";



    public static final String LIST_IMPORT = "java.util.List;";
    public static final String ARRAY_LIST_IMPORT = "java.util.ArrayList;";
    public static final String LINKED_LIST_IMPORT = "java.util.LinkedList;";
    public static final String SET_IMPORT = "java.util.Set;";
    public static final String HASH_SET_IMPORT = "java.util.HashSet;";
    public static final String MAP_IMPORT = "java.util.Map;";
    public static final String HASH_MAP_IMPORT = "java.util.HashMap;";
    public static final String LINKED_HASH_MAP_IMPORT = "java.util.LinkedHashMap;";

    /**
     * Java Doc
     */
    public static final String JAVA_DOC_START = "    /**";
    public static final String JAVA_DOC_END = "     */";
    public static final String JAVA_DOC_LINE = "     * %s";
    public static final String JAVA_DOC_EXAMPLE = "     * Example: %s";
    public static final String JAVA_DOC_CLASS_START = "/**";
    public static final String JAVA_DOC_CLASS_END = "*/";
    public static final String JAVA_DOC_CLASS_LINE = "* %s";

    /**
     * Getters and Setters
     */
    public static final String GETTER = "    public %s get%s() {" +
            lineSeparator() +
            "        return %s;" +
            lineSeparator() + "    }";
    public static final String SETTER = "    public void set%s(%s %s) {" +
            lineSeparator() +
            "        this.%s = %s;" +
            lineSeparator() + "    }";

    /**
     * Constructors
     */
    public static final String ENUM_CONSTRUCTOR = "    %s(%s %s) {" +
            lineSeparator() +
             "    }";

    /**
     * Realizations
     */
    public static final String ARRAY_LIST_REALISATION = "new ArrayList<>()";
    public static final String LINKED_LIST_REALISATION = "new LinkedList<>()";
    public static final String HASH_SET_REALISATION = "new HashSet<>()";
    public static final String HASH_MAP_REALISATION = "new HashMap<>()";
    public static final String LINKED_HASH_MAP_REALISATION = "new LinkedHashMap<>()";

    /**
     * Others
     */
    public static final String FIELD = "    private %s %s;";
    public static final String FIELD_WITH_DEFAULT_VALUE = "    private %s %s = %s;";
    public static final String FIELD_ENUM_WITH_DESCRIPTION = "    private String description;";
    public static final String LIST_TYPE = "List<%s>";
    public static final String SET_TYPE = "Set<%s>";
    public static final String MAP_TYPE = "Map<String, %s>";
    public static final String ENUM_TYPE = "%s(\"%s\")";
    public static final String DELIMITER = "/";
    public static final String TABULATION = "    ";
    public static final String IMPORT = "import ";
    public static final String GROUPS = "groups = ";
    public static final String MESSAGE_PACKAGE_IMPORT = "messages;";
    public static final String COMMON_PACKAGE_IMPORT = "common;";
    public static final String SPACE = " ";
    public static final String PUBLIC_CLASS = "public class ";
    public static final String PUBLIC_ENUM = "public enum ";

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

    public static final Map<String, String> JAVAX_JAVA_TYPES_REQUIRED_IMPORTS = ofEntries(
            entry(NOT_BLANK_ANNOTATION, JAVAX_NOT_BLANK_IMPORT),
            entry(NOT_EMPTY_ANNOTATION, JAVAX_NOT_EMPTY_IMPORT),
            entry(NOT_NULL_ANNOTATION, JAVAX_NOT_NULL_IMPORT),
            entry(SIZE_ANNOTATION, JAVAX_SIZE_IMPORT),
            entry(PATTERN_ANNOTATION_WITHOUT_REGEXP, JAVAX_PATTERN_IMPORT),
            entry(VALID_ANNOTATION, JAVAX_VALID_IMPORT)
    );

    public static final Map<String, String> JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS = ofEntries(
            entry(NOT_BLANK_ANNOTATION, JAKARTA_NOT_BLANK_IMPORT),
            entry(NOT_EMPTY_ANNOTATION, JAKARTA_NOT_EMPTY_IMPORT),
            entry(NOT_NULL_ANNOTATION, JAKARTA_NOT_NULL_IMPORT),
            entry(SIZE_ANNOTATION, JAKARTA_SIZE_IMPORT),
            entry(PATTERN_ANNOTATION_WITHOUT_REGEXP, JAKARTA_PATTERN_IMPORT),
            entry(VALID_ANNOTATION, JAKARTA_VALID_IMPORT)
    );

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
            UUID
    );

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
            entry(ST_SIMPLE_DATE, SIMPLE_DATE),
            entry(ST_UUID, UUID)
    );

    public static final Map<String, Object> JAVA_UPPER_CASE_TYPES_CHECK_CONVERTER = ofEntries(
            entry(STRING, ST_STRING),
            entry(BYTE, ST_BYTE),
            entry(INTEGER, ST_INTEGER),
            entry(LONG,ST_LONG),
            entry(DOUBLE, ST_DOUBLE),
            entry(FLOAT, ST_FLOAT),
            entry(BOOLEAN, ST_BOOLEAN),
            entry(BIG_DECIMAL, ST_BIG_DECIMAL),
            entry(BIG_INTEGER, ST_BIG_INTEGER),
            entry(LOCAL_DATE, ST_LOCAL_DATE),
            entry(LOCAL_DATE_TIME, ST_LOCAL_DATE_TIME),
            entry(SIMPLE_DATE, SIMPLE_DATE),
            entry(UUID, ST_UUID)
    );
}
