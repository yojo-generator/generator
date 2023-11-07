package ru.yojo.codegen.domain;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class VariableProperties {

    private String name;

    private String type;

    private String minLength;

    private String maxLength;

    private String format;

    private String pattern;

    private String description;

    private String enumeration;

    private String example;

    private String items;

    private String reference;

    private String title;

    private String digits;

    private String minimum;

    private String maximum;

    private String defaultProperty;

    private String realisation;

    private String enumNames;

    private boolean isEnum = false;

    private Boolean primitive;

    private String annotationParameter;

    private Set<String> annotationSet = new HashSet<>();

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
        if (realisation != null && realisation.startsWith("ArrayList")) {
            requiredImports.add(ARRAY_LIST_IMPORT);
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

    public String getAnnotationParameter() {
        return annotationParameter;
    }

    public void setAnnotationParameter(String annotationParameter) {
        this.annotationParameter = annotationParameter;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(String primitive) {
        if (primitive != null) {
            this.primitive = Boolean.valueOf(primitive);
        }
    }

    public void setFormat(String format) {
        if (format != null) {
            switch (format) {
                case "date":
                    this.type = LOCAL_DATE;
                    requiredImports.add(LOCAL_DATE_IMPORT);
                    if (items != null) {
                        this.items = LOCAL_DATE;
                        this.type = format(LIST_TYPE, LOCAL_DATE);
                    }
                    break;
                case "date-time":
                    this.type = LOCAL_DATE_TIME;
                    requiredImports.add(LOCAL_DATE_TIME_IMPORT);
                    if (items != null) {
                        this.items = LOCAL_DATE_TIME;
                        this.type = format(LIST_TYPE, LOCAL_DATE_TIME);
                    }
                    break;
                case "offsetDateTime":
                    this.type = OFFSET_DATE_TIME;
                    requiredImports.add(OFFSET_DATE_TIME_IMPORT);
                    if (items != null) {
                        this.items = OFFSET_DATE_TIME;
                        this.type = format(LIST_TYPE, OFFSET_DATE_TIME);
                    }
                    break;
                case "int64":
                    this.type = LONG;
                    if (items != null) {
                        this.items = LONG;
                        this.type = format(LIST_TYPE, LONG);
                    }
                    break;
                case "uuid":
                    this.type = UUID;
                    requiredImports.add(UUID_IMPORT);
                    if (items != null) {
                        this.items = UUID;
                        this.type = format(LIST_TYPE, UUID);
                    }
                    break;
                case "bigDecimal":
                    this.type = BIG_DECIMAL;
                    requiredImports.add(BIG_DECIMAL_IMPORT);
                    if (items != null) {
                        this.items = BIG_DECIMAL;
                        this.type = format(LIST_TYPE, BIG_DECIMAL);
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
        if (items != null) {
            requiredImports.add(LIST_IMPORT);
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

    public String toWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        generateJavaDoc(stringBuilder, getDescription(), getExample());
        getAnnotationSet().forEach(annotation -> {
            stringBuilder.append(lineSeparator())
                    .append(TABULATION)
                    .append(annotation);
        });

        if (defaultProperty != null) {
            if (type.equals(STRING)) {
                defaultProperty = "\"" + defaultProperty + "\"";
            }
            return stringBuilder.append(lineSeparator())
                    .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), getDefaultProperty())).toString();
        }

        if (primitive != null) {
            switch (type) {
                case "Boolean":
                    setType(ST_BOOLEAN);
                    break;
                case "Integer":
                    setType("int");
                    break;
                case "Long":
                    setType("long");
                    break;
                case "Double":
                    setType("double");
                    break;
                case "Float":
                    setType("float");
                    break;
            }
        }

        if (realisation != null) {
            if (type.startsWith("List")) {
                return stringBuilder.append(lineSeparator())
                        .append(format(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), ARRAY_LIST_REALISATION)).toString();
            }
        }

        return stringBuilder.append(lineSeparator())
                .append(format(FIELD, getType(), getName())).toString();
    }
}
