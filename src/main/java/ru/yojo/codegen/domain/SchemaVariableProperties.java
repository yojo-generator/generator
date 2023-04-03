package ru.yojo.codegen.domain;


import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class SchemaVariableProperties {

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

    public void setFormat(String format) {
        if (format != null) {
            switch (format) {
                case "date":
                    this.type = LOCAL_DATE.getValue();
                    requiredImports.add(LOCAL_DATE_IMPORT.getValue());
                    break;
                case "date-time":
                    this.type = LOCAL_DATE_TIME.getValue();
                    requiredImports.add(LOCAL_DATE_TIME_IMPORT.getValue());
                    break;
                case "int64":
                    this.type = LONG.getValue();
                    break;
                case "uuid":
                    this.type = UUID.getValue();
                    requiredImports.add(UUID_IMPORT.getValue());
                    break;
                case "bigDecimal":
                    this.type = BIG_DECIMAL.getValue();
                    requiredImports.add(BIG_DECIMAL_IMPORT.getValue());
                    break;
            }
        }
        this.format = format;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        generateJavaDoc(stringBuilder, description, enumeration, example);
        annotationSet.forEach(annotation -> {
            stringBuilder.append(lineSeparator())
                    .append(TABULATION.getValue())
                    .append(annotation);
        });
        return stringBuilder.append(lineSeparator())
                .append(formatString(FIELD, type, name)).toString();
    }

    /**
     * The method adds the Size annotation
     *
     * @param minLength minLength
     * @param maxLength maxLength
     */
    private static String generateSizeAnnotation(String minLength, String maxLength) {
        if (isNotBlank(minLength) && isNotBlank(maxLength)) {
            return formatString(SIZE_MIN_MAX_ANNOTATION, minLength, maxLength);
        } else if (isNotBlank(minLength)) {
            return formatString(SIZE_MIN_ANNOTATION, minLength);
        } else {
            return formatString(SIZE_MAX_ANNOTATION, maxLength);
        }
    }
}
