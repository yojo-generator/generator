package ru.yojo.codegen.domain;

import java.util.HashSet;
import java.util.Set;

import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Builder for {@link VariableProperties} objects.
 * Provides fluent API to construct VariableProperties instances with required validation.
 *
 * <p>Example usage:
 * <pre>{@code
 * VariableProperties vp = new VariablePropertiesBuilder()
 *     .name("email")
 *     .type("String")
 *     .format("email")
 *     .required(true)
 *     .build();
 * }</pre>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class VariablePropertiesBuilder {

    // Required fields
    private String name;

    // Optional fields
    private String springBootVersion;
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
    private String multipleOf;
    private String minimum;
    private String maximum;
    private String defaultProperty;
    private String originalEnumName;
    private boolean isPolymorph = false;
    private String packageOfExisingObject;
    private String nameOfExisingObject;
    private Set<String> requiredImports = new HashSet<>();
    private Set<String> fieldAnnotations = new HashSet<>();
    private boolean valid = true;
    private boolean nullableAnnotation = false;
    private String collectionType;

    /**
     * Sets the field name (required).
     *
     * @param name Java field name
     * @return this builder
     */
    public VariablePropertiesBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the Spring Boot version (optional).
     *
     * @param springBootVersion version string (e.g., "3.2.0")
     * @return this builder
     */
    public VariablePropertiesBuilder springBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
        return this;
    }

    /**
     * Sets the Java type (optional).
     *
     * @param type Java type (e.g., "String", "List<LocalDate>")
     * @return this builder
     */
    public VariablePropertiesBuilder type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the format hint (optional).
     *
     * @param format format string (e.g., "email", "uuid")
     * @return this builder
     */
    public VariablePropertiesBuilder format(String format) {
        this.format = format;
        return this;
    }

    /**
     * Sets the field description (optional).
     *
     * @param description description text
     * @return this builder
     */
    public VariablePropertiesBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the enum constant name (optional).
     *
     * @param enumeration enum constant
     * @return this builder
     */
    public VariablePropertiesBuilder enumeration(String enumeration) {
        this.enumeration = enumeration;
        return this;
    }

    /**
     * Sets the example value (optional).
     *
     * @param example example text
     * @return this builder
     */
    public VariablePropertiesBuilder example(String example) {
        this.example = example;
        return this;
    }

    /**
     * Sets the items type for collections (optional).
     *
     * @param items element type
     * @return this builder
     */
    public VariablePropertiesBuilder items(String items) {
        this.items = items;
        return this;
    }

    /**
     * Sets the $ref target (optional).
     *
     * @param reference reference target name
     * @return this builder
     */
    public VariablePropertiesBuilder reference(String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * Sets the title (optional).
     *
     * @param title title text
     * @return this builder
     */
    public VariablePropertiesBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the @Digits parameters (optional).
     *
     * @param digits digits string
     * @return this builder
     */
    public VariablePropertiesBuilder digits(String digits) {
        this.digits = digits;
        return this;
    }

    /**
     * Sets the multipleOf value (optional).
     *
     * @param multipleOf multipleOf value
     * @return this builder
     */
    public VariablePropertiesBuilder multipleOf(String multipleOf) {
        this.multipleOf = multipleOf;
        return this;
    }

    /**
     * Sets the minimum value (optional).
     *
     * @param minimum minimum value
     * @return this builder
     */
    public VariablePropertiesBuilder minimum(String minimum) {
        this.minimum = minimum;
        return this;
    }

    /**
     * Sets the maximum value (optional).
     *
     * @param maximum maximum value
     * @return this builder
     */
    public VariablePropertiesBuilder maximum(String maximum) {
        this.maximum = maximum;
        return this;
    }

    /**
     * Sets the default value (optional).
     *
     * @param defaultProperty default value
     * @return this builder
     */
    public VariablePropertiesBuilder defaultProperty(String defaultProperty) {
        this.defaultProperty = defaultProperty;
        return this;
    }

    /**
     * Sets the original enum name (optional).
     *
     * @param originalEnumName original name
     * @return this builder
     */
    public VariablePropertiesBuilder originalEnumName(String originalEnumName) {
        this.originalEnumName = originalEnumName;
        return this;
    }

    /**
     * Sets whether this is a polymorphic field (optional).
     *
     * @param isPolymorph true if polymorphic
     * @return this builder
     */
    public VariablePropertiesBuilder isPolymorph(boolean isPolymorph) {
        this.isPolymorph = isPolymorph;
        return this;
    }

    /**
     * Sets the package of existing object (optional).
     *
     * @param packageOfExisingObject package name
     * @return this builder
     */
    public VariablePropertiesBuilder packageOfExisingObject(String packageOfExisingObject) {
        this.packageOfExisingObject = packageOfExisingObject;
        return this;
    }

    /**
     * Sets the name of existing object (optional).
     *
     * @param nameOfExisingObject object name
     * @return this builder
     */
    public VariablePropertiesBuilder nameOfExisingObject(String nameOfExisingObject) {
        this.nameOfExisingObject = nameOfExisingObject;
        return this;
    }

    /**
     * Adds a required import (optional).
     *
     * @param importStr import string
     * @return this builder
     */
    public VariablePropertiesBuilder addRequiredImport(String importStr) {
        this.requiredImports.add(importStr);
        return this;
    }

    /**
     * Adds a field annotation (optional).
     *
     * @param annotation annotation string
     * @return this builder
     */
    public VariablePropertiesBuilder addFieldAnnotation(String annotation) {
        this.fieldAnnotations.add(annotation);
        return this;
    }

    /**
     * Sets whether the field is valid (optional).
     *
     * @param valid true if valid
     * @return this builder
     */
    public VariablePropertiesBuilder valid(boolean valid) {
        this.valid = valid;
        return this;
    }

    /**
     * Sets whether to add @Nullable annotation (optional).
     *
     * @param nullableAnnotation true if nullable
     * @return this builder
     */
    public VariablePropertiesBuilder nullableAnnotation(boolean nullableAnnotation) {
        this.nullableAnnotation = nullableAnnotation;
        return this;
    }

    /**
     * Sets the collection type (optional).
     *
     * @param collectionType collection type
     * @return this builder
     */
    public VariablePropertiesBuilder collectionType(String collectionType) {
        this.collectionType = collectionType;
        return this;
    }

    /**
     * Builds the VariableProperties instance.
     *
     * @return a new VariableProperties with all configured properties
     * @throws IllegalStateException if required fields are missing
     */
    public VariableProperties build() {
        validateRequiredFields();

        VariableProperties vp = new VariableProperties();
        vp.setName(name);

        if (springBootVersion != null) vp.setSpringBootVersion(springBootVersion);
        if (type != null) vp.setType(type);
        if (minLength != null) vp.setMinLength(minLength);
        if (maxLength != null) vp.setMaxLength(maxLength);
        if (format != null) vp.setFormat(format);
        if (pattern != null) vp.setPattern(pattern);
        if (description != null) vp.setDescription(description);
        if (enumeration != null) vp.setEnumeration(enumeration);
        if (example != null) vp.setExample(example);
        if (items != null) vp.setItems(items);
        if (reference != null) vp.setReference(reference);
        if (title != null) vp.setTitle(title);
        if (digits != null) vp.setDigits(digits);
        if (multipleOf != null) vp.setMultipleOf(multipleOf);
        if (minimum != null) vp.setMinimum(minimum);
        if (maximum != null) vp.setMaximum(maximum);
        if (defaultProperty != null) vp.setDefaultProperty(defaultProperty);
        if (originalEnumName != null) vp.setOriginalEnumName(originalEnumName);
        vp.setPolymorph(isPolymorph);
        if (packageOfExisingObject != null) vp.setPackageOfExisingObject(packageOfExisingObject);
        if (nameOfExisingObject != null) vp.setNameOfExisingObject(nameOfExisingObject);
        if (!requiredImports.isEmpty()) vp.getRequiredImports().addAll(requiredImports);
        if (!fieldAnnotations.isEmpty()) vp.getFieldAnnotations().addAll(fieldAnnotations);
        vp.setValid(valid);
        vp.setNullableAnnotation(nullableAnnotation ? "org.springframework.lang.Nullable" : null);
        if (collectionType != null) vp.setCollectionType(collectionType);

        return vp;
    }

    private void validateRequiredFields() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("name is required for VariableProperties");
        }
    }
}
