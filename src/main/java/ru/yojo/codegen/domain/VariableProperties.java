package ru.yojo.codegen.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

/**
 * Represents a single field/property in a generated DTO (e.g., {@code private String name;}).
 * <p>
 * Holds all metadata needed to generate:
 * <ul>
 *   <li>Type and name</li>
 *   <li>Validation annotations ({@code @Size}, {@code @Pattern}, {@code @Digits}, etc.)</li>
 *   <li>JavaDoc (description, example)</li>
 *   <li>Default value or collection initialization</li>
 *   <li>Enum-related information (constant name, human-readable description)</li>
 *   <li>Required imports (e.g., {@code java.time.LocalDate}, {@code jakarta.validation.constraints.Size})</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class VariableProperties {

    /**
     * Spring Boot version string (e.g., {@code "3.2.0"}) used to select jakarta vs javax validation imports.
     */
    private String springBootVersion;

    /**
     * Java field name (converted to valid Java identifier: camelCase, no keywords).
     */
    private String name;

    /**
     * Java type (e.g., {@code String}, {@code List<LocalDate>}, {@code MyCustomDto}, {@code boolean} for primitives).
     */
    private String type;

    /**
     * Value for {@code @Size(min = ...)} on {@code String} fields.
     */
    private String minLength;

    /**
     * Value for {@code @Size(max = ...)} on {@code String} fields.
     */
    private String maxLength;

    /**
     * Format hint from AsyncAPI spec (e.g., {@code date}, {@code uuid}, {@code int64}).
     * Used to map to correct Java type and imports.
     */
    private String format;

    /**
     * Value for {@code @Pattern(regexp = "...")} validation.
     */
    private String pattern;

    /**
     * Description from the schema; used in field-level JavaDoc.
     */
    private String description;

    /**
     * Enum constant name (e.g., {@code "SUCCESS"}), if field is an enum reference.
     */
    private String enumeration;

    /**
     * Example value from schema; included in JavaDoc as "Example: ...".
     */
    private String example;

    /**
     * For collection types: element type (e.g., {@code LocalDate} for {@code List<LocalDate>}).
     */
    private String items;

    /**
     * Reference target (e.g., {@code "User"}) for {@code $ref} fields.
     */
    private String reference;

    /**
     * Title from schema (rarely used; fallback for description).
     */
    private String title;

    /**
     * {@code @Digits(integer = ..., fraction = ...)} parameters for {@code BigDecimal}.
     */
    private String digits;

    /**
     * Alternative way to calculate {@code @Digits} from {@code multipleOf} precision (e.g., {@code 0.01} → {@code fraction = 2}).
     */
    private String multipleOf;

    /**
     * Value for {@code @Min(...)} on numeric fields.
     */
    private String minimum;

    /**
     * Value for {@code @Max(...)} on numeric fields.
     */
    private String maximum;

    /**
     * Default value literal (e.g., {@code "default"}, {@code 123L}, {@code new Date()}).
     */
    private String defaultProperty;

    /**
     * Collection realization (e.g., {@code ArrayList}, {@code HashSet}).
     * Used to generate initializer: {@code = new ArrayList<>();}.
     */
    private String realisation;

    /**
     * Human-readable description for enum constant (from {@code x-enumNames}).
     */
    private String enumNames;

    /**
     * {@code true} if this property is an enum reference (not a regular field).
     */
    private boolean isEnum = false;

    /**
     * Original enum constant name from schema (e.g., {@code "success"} vs generated {@code "SUCCESS"}).
     */
    private String originalEnumName;

    /**
     * {@code false} for collections/enums where {@code @Valid} should NOT be added.
     */
    private boolean valid = true;

    /**
     * {@code true} if field should be a primitive type (e.g., {@code int} instead of {@code Integer}).
     */
    private Boolean primitive;

    /**
     * Collection type: {@code "list"} (default) or {@code "set"}.
     */
    private String collectionType = "list";

    /**
     * Class name for {@code format: existing} references (e.g., {@code "ExistingClass"}).
     */
    private String nameOfExisingObject;

    /**
     * Package for {@code format: existing} references (e.g., {@code "com.example"}).
     */
    private String packageOfExisingObject;

    /**
     * {@code true} if this property involves polymorphism ({@code oneOf}, {@code allOf}, {@code anyOf}).
     */
    private boolean polymorph = false;

    /**
     * Validation/annotation strings to prepend before the field (e.g., {@code @NotNull}, {@code @Size(min = 1)}).
     */
    private Set<String> annotationSet = new HashSet<>();

    /**
     * Import declarations required by this field (e.g., {@code java.time.LocalDate}, {@code jakarta.validation.constraints.Size}).
     */
    private Set<String> requiredImports = new HashSet<>();

    // ——— Getters & Setters ——— //

    /**
     * Returns the Java field name.
     *
     * @return field name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Java field name.
     *
     * @param name field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the Java type (e.g., {@code String}, {@code List<User>}).
     *
     * @return type name
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the Java type.
     *
     * @param type type name
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the {@code @Size(min = ...)} value.
     *
     * @return min length or {@code null}
     */
    public String getMinLength() {
        return minLength;
    }

    /**
     * Sets the {@code @Size(min = ...)} value.
     *
     * @param minLength min length value
     */
    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    /**
     * Returns the {@code @Size(max = ...)} value.
     *
     * @return max length or {@code null}
     */
    public String getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the {@code @Size(max = ...)} value.
     *
     * @param maxLength max length value
     */
    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Returns the format string (e.g., {@code "date"}, {@code "uuid"}).
     *
     * @return format or {@code null}
     */
    public String getFormat() {
        return format;
    }

    /**
     * Returns the {@code @Pattern} regexp.
     *
     * @return regexp pattern or {@code null}
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Returns the description (used in JavaDoc).
     *
     * @return description or {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the enum constant name (e.g., {@code "SUCCESS"}).
     *
     * @return constant name or {@code null}
     */
    public String getEnumeration() {
        return enumeration;
    }

    /**
     * Returns whether this property is an enum reference.
     *
     * @return {@code true} for enums
     */
    public boolean isEnum() {
        return isEnum;
    }

    /**
     * Sets whether this property is an enum reference.
     *
     * @param anEnum {@code true} for enums
     */
    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    /**
     * Returns the original enum name from schema (e.g., {@code "success"}).
     *
     * @return original name or {@code null}
     */
    public String getOriginalEnumName() {
        return originalEnumName;
    }

    /**
     * Sets the original enum name from schema.
     *
     * @param name original name
     */
    public void setOriginalEnumName(String name) {
        originalEnumName = name;
    }

    /**
     * Sets the enum constant name (e.g., {@code "SUCCESS"}).
     *
     * @param enumeration constant name
     */
    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * Returns the human-readable enum description (from {@code x-enumNames}).
     *
     * @return description or {@code null}
     */
    public String getEnumNames() {
        return enumNames;
    }

    /**
     * Sets the human-readable enum description.
     *
     * @param enumNames description
     */
    public void setEnumNames(String enumNames) {
        this.enumNames = enumNames;
    }

    /**
     * Returns the example value (used in JavaDoc).
     *
     * @return example or {@code null}
     */
    public String getExample() {
        return example;
    }

    /**
     * Returns the schema reference target (e.g., {@code "User"}).
     *
     * @return reference name or {@code null}
     */
    public String getReference() {
        return reference;
    }

    /**
     * Returns the collection type: {@code "list"} or {@code "set"}.
     *
     * @return collection type
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * Sets the collection type.
     *
     * @param collectionType {@code "list"} or {@code "set"}
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * Sets the schema reference target.
     *
     * @param reference reference name (e.g., {@code "User"})
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Sets the annotation set (e.g., {@code @NotNull}, {@code @Size}).
     *
     * @param annotationSet annotations
     */
    public void setAnnotationSet(Set<String> annotationSet) {
        this.annotationSet = annotationSet;
    }

    /**
     * Sets the required imports (e.g., {@code java.time.LocalDate}).
     *
     * @param requiredImports import strings
     */
    public void setRequiredImports(Set<String> requiredImports) {
        this.requiredImports = requiredImports;
    }

    /**
     * Returns the {@code @Digits} parameters.
     *
     * @return digits config (e.g., {@code "integer = 2, fraction = 2"}) or {@code null}
     */
    public String getDigits() {
        return digits;
    }

    /**
     * Sets the {@code @Digits} parameters.
     *
     * @param digits config string
     */
    public void setDigits(String digits) {
        this.digits = digits;
    }

    /**
     * Returns the package for {@code format: existing} objects.
     *
     * @return package name or {@code null}
     */
    public String getPackageOfExisingObject() {
        return packageOfExisingObject;
    }

    /**
     * Sets the package for {@code format: existing} objects.
     *
     * @param packageOfExisingObject package name
     */
    public void setPackageOfExisingObject(String packageOfExisingObject) {
        this.packageOfExisingObject = packageOfExisingObject;
    }

    /**
     * Returns the class name for {@code format: existing} objects.
     *
     * @return class name or {@code null}
     */
    public String getNameOfExisingObject() {
        return nameOfExisingObject;
    }

    /**
     * Sets the class name for {@code format: existing} objects.
     *
     * @param nameOfExisingObject class name
     */
    public void setNameOfExisingObject(String nameOfExisingObject) {
        this.nameOfExisingObject = nameOfExisingObject;
    }

    /**
     * Returns the collection realization (e.g., {@code "ArrayList"}).
     *
     * @return realization name or {@code null}
     */
    public String getRealisation() {
        return realisation;
    }

    /**
     * Sets whether this property involves polymorphism.
     *
     * @param polymorph {@code true} for {@code oneOf}/{@code allOf}/{@code anyOf}
     */
    public void setPolymorph(boolean polymorph) {
        this.polymorph = polymorph;
    }

    /**
     * Returns whether this property involves polymorphism.
     *
     * @return {@code true} for polymorphic fields
     */
    public boolean isPolymorph() {
        return polymorph;
    }

    /**
     * Sets the collection realization and adds corresponding import.
     * <p>
     * Supported values: {@code "ArrayList"}, {@code "LinkedList"}, {@code "HashSet"},
     * {@code "HashMap"}, {@code "LinkedHashMap"}.
     *
     * @param realisation realization name
     */
    public void setRealisation(String realisation) {
        this.realisation = realisation;
        if (realisation != null)
            switch (realisation) {
                case "ArrayList":
                    requiredImports.add(ARRAY_LIST_IMPORT);
                    break;
                case "LinkedList":
                    requiredImports.add(LINKED_LIST_IMPORT);
                    break;
                case "LinkedHashMap":
                    requiredImports.add(LINKED_HASH_MAP_IMPORT);
                    break;
                case "HashMap":
                    requiredImports.add(HASH_MAP_IMPORT);
                    break;
                case "HashSet":
                    requiredImports.add(HASH_SET_IMPORT);
                    break;
            }
    }

    /**
     * Returns the {@code @Min} value.
     *
     * @return minimum or {@code null}
     */
    public String getMinimum() {
        return minimum;
    }

    /**
     * Sets the {@code @Min} value and adds appropriate import/annotation.
     *
     * @param minimum min value
     */
    public void setMinimum(String minimum) {
        this.minimum = minimum;
        if (isNotBlank(minimum) && multipleOf == null) {
            annotationSet.add(generateMinAnnotation(minimum));
            if (springBootVersion != null && springBootVersion.startsWith("3")) {
                requiredImports.add(JAKARTA_MIN_IMPORT);
            } else {
                requiredImports.add(JAVAX_MIN_IMPORT);
            }
        }
    }

    /**
     * Returns the {@code @Max} value.
     *
     * @return maximum or {@code null}
     */
    public String getMaximum() {
        return maximum;
    }

    /**
     * Sets the {@code @Max} value and adds appropriate import/annotation.
     *
     * @param maximum max value
     */
    public void setMaximum(String maximum) {
        this.maximum = maximum;
        if (isNotBlank(maximum) && multipleOf == null) {
            annotationSet.add(generateMaxAnnotation(maximum));
            if (springBootVersion != null && springBootVersion.startsWith("3")) {
                requiredImports.add(JAKARTA_MAX_IMPORT);
            } else {
                requiredImports.add(JAVAX_MAX_IMPORT);
            }
        }
    }

    /**
     * Returns whether this field should be a primitive type.
     *
     * @return {@code true} for {@code int}, {@code boolean}, etc.
     */
    public boolean isPrimitive() {
        return primitive != null && primitive;
    }

    /**
     * Sets whether this field should be a primitive type.
     *
     * @param primitive {@code "true"} or {@code "false"}
     */
    public void setPrimitive(String primitive) {
        if (primitive != null) {
            this.primitive = Boolean.valueOf(primitive);
        }
    }

    /**
     * Sets the format and maps it to the correct Java type and imports.
     * <p>
     * Handles scalar types ({@code string}, {@code date}, {@code uuid}, etc.),
     * numeric types ({@code int32}, {@code long}, {@code big-decimal}), and collections.
     *
     * @param format format string from schema
     */
    public void setFormat(String format) {
        if (format == null) {
            // fallback: bare `type: number` → BigDecimal
            if (NUMBER.equalsIgnoreCase(type)) {
                this.type = BIG_DECIMAL;
                this.format = BIG_DECIMAL;
                format = "big-decimal";
                if (multipleOf != null) {
                    Integer fraction = multipleOf
                            .substring(multipleOf.indexOf('.'), multipleOf.length())
                            .replace(".", "")
                            .length();
                    Integer integer = multipleOf
                            .replace(".", "")
                            .length();
                    digits = "integer = " + integer + ", " + "fraction = " + fraction;
                }
            }
        }
        if (format != null) {
            switch (format) {
                case "string":
                    this.type = STRING;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = STRING;
                        this.type = format(collectionPattern, STRING);
                        this.valid = false;
                    }
                    break;
                case "date":
                    this.type = LOCAL_DATE;
                    requiredImports.add(LOCAL_DATE_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = LOCAL_DATE;
                        this.type = format(collectionPattern, LOCAL_DATE);
                        this.valid = false;
                    }
                    break;
                case "date-time":
                    this.type = OFFSET_DATE_TIME;
                    requiredImports.add(OFFSET_DATE_TIME_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = OFFSET_DATE_TIME;
                        this.type = format(collectionPattern, OFFSET_DATE_TIME);
                        this.valid = false;
                    }
                    break;
                case "local-date-time":
                    this.type = LOCAL_DATE_TIME;
                    requiredImports.add(LOCAL_DATE_TIME_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = LOCAL_DATE_TIME;
                        this.type = format(collectionPattern, LOCAL_DATE_TIME);
                        this.valid = false;
                    }
                    break;
                case "simple-date":
                    this.type = SIMPLE_DATE;
                    requiredImports.add(SIMPLE_DATE_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = SIMPLE_DATE;
                        this.type = format(collectionPattern, SIMPLE_DATE);
                        this.valid = false;
                    }
                    break;
                case "int64":
                case "long":
                    this.type = LONG;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = LONG;
                        this.type = format(collectionPattern, LONG);
                        this.valid = false;
                    }
                    if (digits != null) {
                        if (springBootVersion != null && springBootVersion.startsWith("3")) {
                            requiredImports.add(JAKARTA_DIGITS_IMPORT);
                        } else {
                            requiredImports.add(JAVAX_DIGITS_IMPORT);
                        }
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
                case "int32":
                case "integer":
                    this.type = INTEGER;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = INTEGER;
                        this.type = format(collectionPattern, INTEGER);
                        this.valid = false;
                    }
                    if (digits != null) {
                        if (springBootVersion != null && springBootVersion.startsWith("3")) {
                            requiredImports.add(JAKARTA_DIGITS_IMPORT);
                        } else {
                            requiredImports.add(JAVAX_DIGITS_IMPORT);
                        }
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
                case "byte":
                    this.type = BYTE;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = BYTE;
                        this.type = format(collectionPattern, BYTE);
                        this.valid = false;
                    }
                    if (digits != null) {
                        if (springBootVersion != null && springBootVersion.startsWith("3")) {
                            requiredImports.add(JAKARTA_DIGITS_IMPORT);
                        } else {
                            requiredImports.add(JAVAX_DIGITS_IMPORT);
                        }
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
                case "double":
                    this.type = DOUBLE;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = DOUBLE;
                        this.type = format(collectionPattern, DOUBLE);
                        this.valid = false;
                    }
                    break;
                case "float":
                    this.type = FLOAT;
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = FLOAT;
                        this.type = format(collectionPattern, FLOAT);
                        this.valid = false;
                    }
                    break;
                case "uuid":
                    this.type = UUID;
                    requiredImports.add(UUID_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = UUID;
                        this.type = format(collectionPattern, UUID);
                        this.valid = false;
                    }
                    break;
                case "uri":
                    this.type = URI;
                    requiredImports.add(URI_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = URI;
                        this.type = format(collectionPattern, URI);
                        this.valid = false;
                    }
                    break;
                case "big-decimal":
                    this.type = BIG_DECIMAL;
                    requiredImports.add(BIG_DECIMAL_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = BIG_DECIMAL;
                        this.type = format(collectionPattern, BIG_DECIMAL);
                        this.valid = false;
                    }
                    if (digits != null) {
                        if (springBootVersion != null && springBootVersion.startsWith("3")) {
                            requiredImports.add(JAKARTA_DIGITS_IMPORT);
                        } else {
                            requiredImports.add(JAVAX_DIGITS_IMPORT);
                        }
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
                case "big-integer":
                    this.type = BIG_INTEGER;
                    requiredImports.add(BIG_INTEGER_IMPORT);
                    if (items != null) {
                        String collectionPattern = LIST_TYPE;
                        if ("set".equalsIgnoreCase(collectionType)) {
                            collectionPattern = SET_TYPE;
                        }
                        this.items = BIG_INTEGER;
                        this.type = format(collectionPattern, BIG_INTEGER);
                        this.valid = false;
                    }
                    if (digits != null) {
                        if (springBootVersion != null && springBootVersion.startsWith("3")) {
                            requiredImports.add(JAKARTA_DIGITS_IMPORT);
                        } else {
                            requiredImports.add(JAVAX_DIGITS_IMPORT);
                        }
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
            }
            this.format = format;
        }
    }

    /**
     * Sets the {@code @Pattern} regexp and adds appropriate import/annotation.
     *
     * @param pattern regexp string
     */
    public void setPattern(String pattern) {
        if (pattern != null) {
            annotationSet.add(format(PATTERN_ANNOTATION, pattern));
            if (springBootVersion != null && springBootVersion.startsWith("3")) {
                requiredImports.add(JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(PATTERN_ANNOTATION, "(")));
            } else {
                requiredImports.add(JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(PATTERN_ANNOTATION, "(")));
            }
        }
        this.pattern = pattern;
    }

    /**
     * Sets the description (used in JavaDoc).
     *
     * @param description field description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the example value (used in JavaDoc).
     *
     * @param example example value
     */
    public void setExample(String example) {
        this.example = example;
    }

    /**
     * Returns the collection element type.
     *
     * @return item type or {@code null}
     */
    public String getItems() {
        return items;
    }

    /**
     * Sets the collection element type and adds {@code List} or {@code Set} import.
     *
     * @param items element type
     */
    public void setItems(String items) {
        if (items != null && "list".equals(collectionType)) {
            requiredImports.add(LIST_IMPORT);
        }
        if (items != null && "set".equals(collectionType)) {
            requiredImports.add(SET_IMPORT);
        }
        this.items = items;
    }

    /**
     * Sets {@code @Size} annotation and adds import based on min/max values.
     *
     * @param min minimum length
     * @param max maximum length
     */
    public void setMinMaxLength(String min, String max) {
        if (isNoneEmpty(min) || isNoneEmpty(max)) {
            annotationSet.add(generateSizeAnnotation(min, max));
            if (springBootVersion != null && springBootVersion.startsWith("3")) {
                requiredImports.add(
                        JAKARTA_JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(
                                SIZE_MIN_MAX_ANNOTATION, "(")));
            } else {
                requiredImports.add(
                        JAVAX_JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(
                                SIZE_MIN_MAX_ANNOTATION, "(")));
            }
        }
        this.minLength = min;
        this.maxLength = max;
    }

    /**
     * Returns the set of validation/other annotations.
     *
     * @return annotation strings
     */
    public Set<String> getAnnotationSet() {
        return annotationSet;
    }

    /**
     * Returns the set of required import declarations.
     *
     * @return imports
     */
    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    /**
     * Adds a single import declaration.
     *
     * @param preparedImport import string (e.g., {@code "java.time.LocalDate;"})
     */
    public void addRequiredImports(String preparedImport) {
        if (preparedImport != null) {
            requiredImports.add(preparedImport);
        }
    }

    /**
     * Returns the title (fallback for description).
     *
     * @return title or {@code null}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title field title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the default value literal.
     *
     * @return default expression or {@code null}
     */
    public String getDefaultProperty() {
        return defaultProperty;
    }

    /**
     * Sets the default value literal.
     *
     * @param defaultProperty default expression
     */
    public void setDefaultProperty(String defaultProperty) {
        this.defaultProperty = defaultProperty;
    }

    /**
     * Returns whether {@code @Valid} should be added.
     *
     * @return {@code true} for nested objects (excluding collections/enums)
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets whether {@code @Valid} should be added.
     *
     * @param valid {@code false} for collections/enums
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Sets the {@code multipleOf} value (used to infer {@code @Digits} precision).
     *
     * @param multipleOf multiple-of constraint
     */
    public void setMultipleOf(String multipleOf) {
        this.multipleOf = multipleOf;
    }

    /**
     * Returns the {@code multipleOf} value.
     *
     * @return multiple-of constraint or {@code null}
     */
    public String getMultipleOf() {
        return multipleOf;
    }

    /**
     * Generates the full field declaration, including:
     * <ul>
     *   <li>JavaDoc (description + example)</li>
     *   <li>Validation annotations (e.g., {@code @NotNull})</li>
     *   <li>Type and name</li>
     *   <li>Default value or collection initialization, if specified</li>
     * </ul>
     *
     * @return Java source code for the field (e.g., {@code private String name;})
     */
    public String toWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        generateJavaDoc(stringBuilder, getDescription(), getExample());
        Comparator<String> stringComparator = (a, b) -> Integer.compare(a.length(), b.length());
        getAnnotationSet().stream().filter(annotation -> !isPrimitive()).sorted().sorted(stringComparator).forEach(annotation -> {
            stringBuilder.append(lineSeparator())
                    .append(TABULATION)
                    .append(annotation);
        });

        if (primitive != null) {
            switch (type) {
                case BOOLEAN:
                    setType("boolean");
                    break;
                case BYTE:
                    setType("byte");
                    break;
                case INTEGER:
                    setType("int");
                    break;
                case LONG:
                    setType("long");
                    break;
                case DOUBLE:
                    setType("double");
                    break;
                case FLOAT:
                    setType("float");
                    break;
            }
        }

        if (defaultProperty != null) {
            if (defaultProperty.equals("new")) {
                switch (type) {
                    case SIMPLE_DATE:
                        defaultProperty = "new Date()";
                        break;
                    case STRING:
                        defaultProperty = "new String()";
                        break;
                }
            } else {
                switch (type) {
                    case UUID:
                        defaultProperty = "UUID.fromString(" + "\"" + defaultProperty + "\"" + ")";
                        break;
                    case STRING:
                        defaultProperty = defaultProperty.replace("\"", "");
                        defaultProperty = "\"" + defaultProperty + "\"";
                        break;
                }
            }
            stringBuilder.append(lineSeparator())
                    .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), getDefaultProperty()));
            return stringBuilder.toString();
        }

        if (realisation != null) {
            if (type.startsWith("List")) {
                switch (realisation) {
                    case "ArrayList":
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), ARRAY_LIST_REALISATION)).toString();
                    case "LinkedList":
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), LINKED_LIST_REALISATION)).toString();
                }
            }
            if (type.startsWith("Set")) {
                switch (realisation) {
                    case "HashSet":
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), HASH_SET_REALISATION)).toString();
                }
            }
            if (type.startsWith("Map")) {
                switch (realisation) {
                    case "HashMap":
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), HASH_MAP_REALISATION)).toString();
                    case "LinkedHashMap":
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), LINKED_HASH_MAP_REALISATION)).toString();
                }
            }
        }

        return stringBuilder.append(lineSeparator())
                .append(format(FIELD, getType(), getName())).toString();
    }

    /**
     * Returns the Spring Boot version string.
     *
     * @return version (e.g., {@code "2.7.0"} or {@code "3.2.0"})
     */
    public String getSpringBootVersion() {
        return springBootVersion;
    }

    /**
     * Sets the Spring Boot version string.
     *
     * @param springBootVersion version string
     */
    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }
}