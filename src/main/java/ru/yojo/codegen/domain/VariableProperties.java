package ru.yojo.codegen.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class VariableProperties {

    /**
     * Property name
     */
    private String name;

    /**
     * Property type
     */
    private String type;

    /**
     * Property used in annotation @Size for Strings
     */
    private String minLength;

    /**
     * Property used in annotation @Size for Strings
     */
    private String maxLength;

    /**
     * Property used for specify type of propety
     */
    private String format;

    /**
     * Property used in annotation @Pattern for Strings
     */
    private String pattern;

    /**
     * Property used for JavaDoc
     */
    private String description;

    private String enumeration;

    /**
     * Property used for JavaDoc
     */
    private String example;

    /**
     * Property used for generate List of smth.
     */
    private String items;

    private String reference;

    /**
     * Property used for JavaDoc
     */
    private String title;

    /**
     * Property used in annotation @Digits for BigDecimal
     */
    private String digits;

    /**
     * Property used in annotation @Min for Integers
     */
    private String minimum;

    /**
     * Property used in annotation @Max for Integers
     */
    private String maximum;

    /**
     * Property used to generate with default value
     */
    private String defaultProperty;

    /**
     * Property used to generate with specify realisation of Collection
     */
    private String realisation;

    private String enumNames;

    private boolean isEnum = false;

    private boolean valid = true;
    /**
     * Property used to generate simple variable
     */
    private Boolean primitive;

    /**
     *
     */
    private String collectionType = "list";

    /**
     * Set of required annotation for variable
     */
    private Set<String> annotationSet = new HashSet<>();

    /**
     * Set of required imports for variable
     */
    private Set<String> requiredImports = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getFormat() {
        return format;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getEnumNames() {
        return enumNames;
    }

    public void setEnumNames(String enumNames) {
        this.enumNames = enumNames;
    }

    public String getExample() {
        return example;
    }

    public String getReference() {
        return reference;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setAnnotationSet(Set<String> annotationSet) {
        this.annotationSet = annotationSet;
    }

    public void setRequiredImports(Set<String> requiredImports) {
        this.requiredImports = requiredImports;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    public String getRealisation() {
        return realisation;
    }

    public void setRealisation(String realisation) {
        this.realisation = realisation;
        if (realisation != null)
            switch (realisation) {
                case ("ArrayList"):
                    requiredImports.add(ARRAY_LIST_IMPORT);
                    break;
                case ("LinkedList"):
                    requiredImports.add(LINKED_LIST_IMPORT);
                    break;
                case ("LinkedHashMap"):
                    requiredImports.add(LINKED_HASH_MAP_IMPORT);
                    break;
                case ("HashMap"):
                    requiredImports.add(HASH_MAP_IMPORT);
                    break;
                case ("HashSet"):
                    requiredImports.add(HASH_SET_IMPORT);
                    break;
            }
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
        if (isNotBlank(minimum)) {
            annotationSet.add(generateMinAnnotation(minimum));
            requiredImports.add(MIN_IMPORT);
        }
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
        if (isNotBlank(maximum)) {
            annotationSet.add(generateMaxAnnotation(maximum));
            requiredImports.add(MAX_IMPORT);
        }
    }

    public boolean isPrimitive() {
        return primitive != null && primitive == true;
    }

    public void setPrimitive(String primitive) {
        if (primitive != null) {
            this.primitive = Boolean.valueOf(primitive);
        }
    }

    public void setFormat(String format) {
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
                case "offsetDateTime":
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
                        requiredImports.add(DIGITS_IMPORT);
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
                        requiredImports.add(DIGITS_IMPORT);
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
                        requiredImports.add(DIGITS_IMPORT);
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
                case "bigDecimal":
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
                        requiredImports.add(DIGITS_IMPORT);
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
                case "bigInteger":
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
                        requiredImports.add(DIGITS_IMPORT);
                        annotationSet.add(format(DIGITS_ANNOTATION, digits));
                    }
                    break;
            }
            this.format = format;
        }
    }

    public void setPattern(String pattern) {
        if (pattern != null) {
            annotationSet.add(format(PATTERN_ANNOTATION, pattern));
            requiredImports.add(JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(PATTERN_ANNOTATION, "(")));
        }
        this.pattern = pattern;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        if (items != null && "list".equals(collectionType)) {
            requiredImports.add(LIST_IMPORT);
        }
        if (items != null && "set".equals(collectionType)) {
            requiredImports.add(SET_IMPORT);
        }
        this.items = items;
    }

    public void setMinMaxLength(String min, String max) {
        if (isNoneEmpty(min) || isNoneEmpty(max)) {
            annotationSet.add(generateSizeAnnotation(min, max));
            requiredImports.add(
                    JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(
                            SIZE_MIN_MAX_ANNOTATION, "(")));
        }
        this.minLength = min;
        this.maxLength = max;
    }

    public Set<String> getAnnotationSet() {
        return annotationSet;
    }

    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultProperty() {
        return defaultProperty;
    }

    public void setDefaultProperty(String defaultProperty) {
        this.defaultProperty = defaultProperty;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

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
            switch (type) {
                case UUID:
                    defaultProperty = "UUID.fromString(" + "\"" + defaultProperty + "\"" + ")";
                    break;
                case STRING:
                    defaultProperty = defaultProperty.replace("\"", "");
                    defaultProperty = "\"" + defaultProperty + "\"";
                    break;
            }
            if (isPrimitive()) {
                stringBuilder.append(lineSeparator())
                        .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), getDefaultProperty())).toString();
            } else {
                stringBuilder.append(lineSeparator())
                        .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), getDefaultProperty())).toString();
            }
            return stringBuilder.toString();
        }

        if (realisation != null) {
            if (type.startsWith("List")) {
                switch (realisation) {
                    case ("ArrayList"): {
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), ARRAY_LIST_REALISATION)).toString();
                    }
                    case ("LinkedList"): {
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), LINKED_LIST_REALISATION)).toString();
                    }
                }
            }
            if (type.startsWith("Set")) {
                switch (realisation) {
                    case ("HashSet"): {
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), HASH_SET_REALISATION)).toString();
                    }
                }
            }
            if (type.startsWith("Map")) {
                switch (realisation) {
                    case ("HashMap"):
                        return stringBuilder.append(lineSeparator())
                                .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), HASH_MAP_REALISATION)).toString();
                    case ("LinkedHashMap"):
                    return stringBuilder.append(lineSeparator())
                            .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), LINKED_HASH_MAP_REALISATION)).toString();

                }
            }
        }

        return stringBuilder.append(lineSeparator())
                .append(format(FIELD, getType(), getName())).toString();
    }
}
