package ru.yojo.codegen.constants;

import java.util.List;
import java.util.Map;

import static java.lang.System.lineSeparator;

public enum ConstantsEnum {

    /**
     * Attributes Yaml
     */
    PROPERTIES("properties"),
    TYPE("type"),
    FORMAT("format"),
    EXAMPLE("example"),
    DESCRIPTION("description"),
    REFERENCE("$ref"),
    REQUIRED("required"),
    MAX_LENGTH("maxLength"),
    MIN_LENGTH("minLength"),
    ITEMS("items"),
    PATTERN("pattern"),
    ENUMERATION("enum"),
    X_ENUM_NAMES("x-enumNames"),
    DIGITS("digits"),
    MINIMUM("minimum"),
    MAXIMUM("maximum"),
    ARRAY("array"),
    OBJECT("object"),
    NAME("name"),
    TITLE("title"),
    SUMMARY("summary"),
    PAYLOAD("payload"),
    TAGS("tags"),
    ADDITIONAL_PROPERTIES("additionalProperties"),
    DEFAULT("default"),

    /**
     * Custom YAML attributes
     */
    REALISATION("realisation"),
    PRIMITIVE("primitive"),
    VALIDATION_GROUPS("validationGroups"),
    VALIDATION_GROUPS_IMPORTS("validationGroupsImports"),
    VALIDATE_BY_GROUPS("validateByGroups"),
    EXTENDS("extends"),
    IMPLEMENTS("implements"),
    FROM_CLASS("fromClass"),
    FROM_PACKAGE("fromPackage"),
    FROM_INTERFACE("fromInterface"),

    /**
     * Schema Types
     */
    ST_LOCAL_DATE("date"),
    ST_LOCAL_DATE_TIME("date-time"),
    ST_LONG("long"),
    ST_INTEGER("integer"),
    ST_UUID("uuid"),
    ST_BOOLEAN("boolean"),
    ST_STRING("string"),
    ST_OBJECT_TYPE("object"),
    ST_BIG_DECIMAL("bigDecimal"),

    /**
     * Annotations
     */
    NOT_EMPTY_ANNOTATION("@NotEmpty"),
    NOT_NULL_ANNOTATION("@NotNull"),
    NOT_BLANK_ANNOTATION("@NotBlank"),
    SIZE_ANNOTATION("@Size"),
    SIZE_MIN_MAX_ANNOTATION("@Size(min = %s, max = %s)"),
    SIZE_MAX_ANNOTATION("@Size(max = %s)"),
    SIZE_MIN_ANNOTATION("@Size(min = %s)"),
    PATTERN_ANNOTATION("@Pattern(regexp = \"%s\")"),
    PATTERN_ANNOTATION_WITHOUT_REGEXP("@Pattern"),
    VALID_ANNOTATION("@Valid"),
    DIGITS_ANNOTATION("@Digits(%s)"),
    MINIMUM_ANNOTATION("@Min(%s)"),
    MAXIMUM_ANNOTATION("@Max(%s)"),

    /**
     * Lombok annotations
     */
    LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION("@AllArgsConstructor"),
    LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION("@NoArgsConstructor"),
    LOMBOK_DATA_ANNOTATION("@Data"),
    LOMBOK_GETTER_ANNOTATION("@Getter"),
    LOMBOK_ACCESSORS_ANNOTATION("@Accessors(fluent = true, chain = true)"),
    LOMBOK_ACCESSORS_EMPTY_ANNOTATION("@Accessors"),
    LOMBOK_ACCESSORS_FLUENT_ANNOTATION("@Accessors(fluent = true)"),
    LOMBOK_ACCESSORS_CHAIN_ANNOTATION("@Accessors(chain = true)"),

    /**
     * Lombok Imports
     */
    LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT("lombok.AllArgsConstructor;"),
    LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT("lombok.NoArgsConstructor;"),
    LOMBOK_DATA_IMPORT("lombok.Data;"),
    LOMBOK_GETTER_IMPORT("lombok.Getter;"),
    LOMBOK_ACCESSORS_IMPORT("lombok.experimental.Accessors;"),

    /**
     * Java Types
     */
    LOCAL_DATE("LocalDate"),
    LOCAL_DATE_TIME("LocalDateTime"),
    OFFSET_DATE_TIME("OffsetDateTime"),
    LONG("Long"),
    INTEGER("Integer"),
    UUID("UUID"),
    BOOLEAN("Boolean"),
    STRING("String"),
    OBJECT_TYPE("Object"),
    BIG_DECIMAL("BigDecimal"),

    /**
     * Packages
     */
    BIG_DECIMAL_IMPORT("java.math.BigDecimal;"),
    LOCAL_DATE_IMPORT("java.time.LocalDate;"),
    LOCAL_DATE_TIME_IMPORT("java.time.LocalDateTime;"),
    OFFSET_DATE_TIME_IMPORT("java.time.OffsetDateTime;"),
    UUID_IMPORT("java.util.UUID;"),
    NOT_BLANK_IMPORT("javax.validation.constraints.NotBlank;"),
    NOT_EMPTY_IMPORT("javax.validation.constraints.NotEmpty;"),
    NOT_NULL_IMPORT("javax.validation.constraints.NotNull;"),
    SIZE_IMPORT("javax.validation.constraints.Size;"),
    PATTERN_IMPORT("javax.validation.constraints.Pattern;"),
    VALID_IMPORT("javax.validation.Valid;"),
    LIST_IMPORT("java.util.List;"),
    ARRAY_LIST_IMPORT("java.util.ArrayList;"),
    MAP_IMPORT("java.util.Map;"),
    DIGITS_IMPORT("javax.validation.constraints.Digits;"),
    MIN_IMPORT("javax.validation.constraints.Min;"),
    MAX_IMPORT("javax.validation.constraints.Max;"),

    /**
     * Java Doc
     */
    JAVA_DOC_START("    /**"),
    JAVA_DOC_END("     */"),
    JAVA_DOC_LINE("     * %s"),
    JAVA_DOC_EXAMPLE("     * Example: %s"),
    JAVA_DOC_CLASS_START("/**"),
    JAVA_DOC_CLASS_END("*/"),
    JAVA_DOC_CLASS_LINE("* %s"),

    /**
     * Getters and Setters
     */
    GETTER("    public %s get%s() {" +
            lineSeparator() +
            "        return %s;" +
            lineSeparator() + "    }"),
    SETTER("    public void set%s(%s %s) {" +
            lineSeparator() +
            "        this.%s = %s;" +
            lineSeparator() + "    }"),

    /**
     * Others
     */
    FIELD("    private %s %s;"),
    FIELD_WITH_DEFAULT_VALUE("    private %s %s = %s;"),
    FIELD_ENUM_WITH_DESCRIPTION("    private String description;"),
    ARRAY_LIST_REALISATION("new ArrayList<>()"),
    LIST_TYPE("List<%s>"),
    MAP_TYPE("Map<String, %s>"),
    ENUM_TYPE("%s(\"%s\")"),
    DELIMETER("/"),
    TABULATION("    "),
    IMPORT("import "),
    GROUPS("groups = "),
    MESSAGE_PACKAGE_IMPORT("messages;"),
    COMMON_PACKAGE_IMPORT("common;");

    public String getValue() {
        return value;
    }

    ConstantsEnum(String value) {
        this.value = value;
    }

    public static final Map<String, String> JAVA_TYPES_REQUIRED_ANNOTATIONS = Map.of(
            STRING.value, NOT_BLANK_ANNOTATION.value,
            INTEGER.value, NOT_NULL_ANNOTATION.value,
            LONG.value, NOT_NULL_ANNOTATION.value,
            BOOLEAN.value, NOT_NULL_ANNOTATION.value,
            LOCAL_DATE.value, NOT_NULL_ANNOTATION.value,
            LOCAL_DATE_TIME.value, NOT_NULL_ANNOTATION.value,
            BIG_DECIMAL.value, NOT_NULL_ANNOTATION.value,
            UUID.value, NOT_NULL_ANNOTATION.value,
            OBJECT_TYPE.value, NOT_NULL_ANNOTATION.value);

    public static final Map<String, String> JAVA_TYPES_REQUIRED_IMPORTS = Map.of(
            NOT_BLANK_ANNOTATION.value, NOT_BLANK_IMPORT.value,
            NOT_EMPTY_ANNOTATION.value, NOT_EMPTY_IMPORT.value,
            NOT_NULL_ANNOTATION.value, NOT_NULL_IMPORT.value,
            SIZE_ANNOTATION.value, SIZE_IMPORT.value,
            PATTERN_ANNOTATION_WITHOUT_REGEXP.value, PATTERN_IMPORT.value,
            VALID_ANNOTATION.value, VALID_IMPORT.value);

    public static final List<String> JAVA_DEFAULT_TYPES = List.of(
            STRING.value,
            INTEGER.value,
            LONG.value,
            BOOLEAN.value,
            BIG_DECIMAL.value,
            LOCAL_DATE.value,
            LOCAL_DATE_TIME.value,
            UUID.value
    );

    public static final Map<String, Object> JAVA_DEFAULT_SCHEMA_TYPES = Map.of(
            ST_STRING.value, STRING.value,
            ST_INTEGER.value, INTEGER.value,
            ST_LONG.value, LONG.value,
            ST_BOOLEAN.value, BOOLEAN.value,
            ST_BIG_DECIMAL.value, BIG_DECIMAL.value,
            ST_LOCAL_DATE.value, LOCAL_DATE.value,
            ST_LOCAL_DATE_TIME.value, LOCAL_DATE_TIME.value,
            ST_UUID.value, UUID.value
    );
    private final String value;

    public static String formatString(ConstantsEnum constantsEnum, Object... strings) {
        return String.format(constantsEnum.getValue(), strings);
    }
}
