package ru.yojo.codegen.domain;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
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
            requiredImports.add(ARRAY_LIST_IMPORT.getValue());
        }
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
        if (isNotBlank(minimum)) {
            annotationSet.add(generateMinAnnotation(minimum));
            requiredImports.add(MIN_IMPORT.getValue());
        }
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
        if (isNotBlank(maximum)) {
            annotationSet.add(generateMaxAnnotation(maximum));
            requiredImports.add(MAX_IMPORT.getValue());
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
                    this.type = LOCAL_DATE.getValue();
                    requiredImports.add(LOCAL_DATE_IMPORT.getValue());
                    if (items != null) {
                        this.items = LOCAL_DATE.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), LOCAL_DATE.getValue());
                    }
                    break;
                case "date-time":
                    this.type = LOCAL_DATE_TIME.getValue();
                    requiredImports.add(LOCAL_DATE_TIME_IMPORT.getValue());
                    if (items != null) {
                        this.items = LOCAL_DATE_TIME.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), LOCAL_DATE_TIME.getValue());
                    }
                    break;
                case "offsetDateTime":
                    this.type = OFFSET_DATE_TIME.getValue();
                    requiredImports.add(OFFSET_DATE_TIME_IMPORT.getValue());
                    if (items != null) {
                        this.items = OFFSET_DATE_TIME.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), OFFSET_DATE_TIME.getValue());
                    }
                    break;
                case "int64":
                    this.type = LONG.getValue();
                    if (items != null) {
                        this.items = LONG.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), LONG.getValue());
                    }
                    break;
                case "uuid":
                    this.type = UUID.getValue();
                    requiredImports.add(UUID_IMPORT.getValue());
                    if (items != null) {
                        this.items = UUID.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), UUID.getValue());
                    }
                    break;
                case "bigDecimal":
                    this.type = BIG_DECIMAL.getValue();
                    requiredImports.add(BIG_DECIMAL_IMPORT.getValue());
                    if (items != null) {
                        this.items = BIG_DECIMAL.getValue();
                        this.type = String.format(LIST_TYPE.getValue(), BIG_DECIMAL.getValue());
                    }
                    if (digits != null) {
                        requiredImports.add(DIGITS_IMPORT.getValue());
                        annotationSet.add(String.format(DIGITS_ANNOTATION.getValue(), digits));
                    }
                    break;
            }
            this.format = format;
        }
    }

    public void setPattern(String pattern) {
        if (pattern != null) {
            annotationSet.add(String.format(PATTERN_ANNOTATION.getValue(), pattern));
            requiredImports.add(JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(PATTERN_ANNOTATION.getValue(), "(")));
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
            requiredImports.add(LIST_IMPORT.getValue());
        }
        this.items = items;
    }

    public void setMinMaxLength(String min, String max) {
        if (isNoneEmpty(min) || isNoneEmpty(max)) {
            annotationSet.add(generateSizeAnnotation(min, max));
            requiredImports.add(
                    JAVA_TYPES_REQUIRED_IMPORTS.get(substringBefore(
                            SIZE_MIN_MAX_ANNOTATION.getValue(), "(")));
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
                    .append(TABULATION.getValue())
                    .append(annotation);
        });

        if (defaultProperty != null) {
            if (type.equals(STRING.getValue())) {
                defaultProperty = "\"" + defaultProperty + "\"";
            }
            return stringBuilder.append(lineSeparator())
                    .append(formatString(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), getDefaultProperty())).toString();
        }

        if (primitive != null) {
            switch (type) {
                case "Boolean":
                    setType(ST_BOOLEAN.getValue());
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
                        .append(formatString(FIELD_WITH_DEFAULT_VALUE, getType(), getName(), ARRAY_LIST_REALISATION.getValue())).toString();
            }
        }

        return stringBuilder.append(lineSeparator())
                .append(formatString(FIELD, getType(), getName())).toString();
    }
}
