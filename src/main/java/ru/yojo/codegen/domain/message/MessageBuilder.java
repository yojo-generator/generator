package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * Builder for {@link Message} objects.
 * Provides fluent API to construct Message instances with required validation.
 *
 * <p>Example usage:
 * <pre>{@code
 * Message message = new MessageBuilder()
 *     .name("UserSignedUp")
 *     .messagePackageName("com.example.api.messages;")
 *     .commonPackageName("com.example.api.common;")
 *     .lombokProperties(lombokProps)
 *     .addField(variableProperties)
 *     .build();
 * }</pre>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class MessageBuilder {

    // Required fields
    private String messageName;
    private String messagePackageName;

    // Optional fields
    private String summary;
    private FillParameters fillParameters;
    private String commonPackageName;
    private LombokProperties lombokProperties;
    private String extendsFrom;
    private Set<String> implementsFrom = new HashSet<>();
    private Set<String> importSet = new HashSet<>();
    private Set<String> classAnnotations = new HashSet<>();
    private String pathForGenerateMessage;

    /**
     * Sets the message name (required).
     *
     * @param messageName class name (e.g., "UserSignedUp")
     * @return this builder
     */
    public MessageBuilder name(String messageName) {
        this.messageName = messageName;
        return this;
    }

    /**
     * Sets the message package (required).
     *
     * @param messagePackageName full package with trailing semicolon
     * @return this builder
     */
    public MessageBuilder messagePackageName(String messagePackageName) {
        this.messagePackageName = messagePackageName;
        return this;
    }

    /**
     * Sets the summary (optional).
     *
     * @param summary summary text
     * @return this builder
     */
    public MessageBuilder summary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * Sets the field and validation container (optional).
     *
     * @param fillParameters field definitions
     * @return this builder
     */
    public MessageBuilder fillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
        return this;
    }

    /**
     * Sets the common package (optional).
     *
     * @param commonPackageName full package with trailing semicolon
     * @return this builder
     */
    public MessageBuilder commonPackageName(String commonPackageName) {
        this.commonPackageName = commonPackageName;
        return this;
    }

    /**
     * Sets the Lombok configuration (optional).
     *
     * @param lombokProperties Lombok settings
     * @return this builder
     */
    public MessageBuilder lombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
        return this;
    }

    /**
     * Sets the superclass to extend (optional).
     *
     * @param extendsFrom superclass name
     * @return this builder
     */
    public MessageBuilder extendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
        return this;
    }

    /**
     * Adds an interface to implement (optional).
     *
     * @param interfaceName interface name
     * @return this builder
     */
    public MessageBuilder addImplementsFrom(String interfaceName) {
        this.implementsFrom.add(interfaceName);
        return this;
    }

    /**
     * Adds a custom import (optional).
     *
     * @param importName import statement
     * @return this builder
     */
    public MessageBuilder addImport(String importName) {
        this.importSet.add(importName);
        return this;
    }

    /**
     * Adds a class-level annotation (optional).
     *
     * @param annotation annotation string
     * @return this builder
     */
    public MessageBuilder addClassAnnotation(String annotation) {
        this.classAnnotations.add(annotation);
        return this;
    }

    /**
     * Sets the custom path for message generation (optional).
     *
     * @param pathForGenerateMessage custom path
     * @return this builder
     */
    public MessageBuilder pathForGenerateMessage(String pathForGenerateMessage) {
        this.pathForGenerateMessage = pathForGenerateMessage;
        return this;
    }

    /**
     * Returns whether this builder has an {@code extends} value set.
     *
     * @return true if extendsFrom has been set
     */
    public boolean hasExtendsFrom() {
        return extendsFrom != null && !extendsFrom.trim().isEmpty();
    }

    /**
     * Returns the {@code extends} value, or {@code null} if not set.
     *
     * @return extendsFrom value
     */
    public String getExtendsFrom() {
        return extendsFrom;
    }

    /**
     * Returns whether this builder has any {@code implements} values.
     *
     * @return true if implementsFrom is not empty
     */
    public boolean hasImplementsFrom() {
        return !implementsFrom.isEmpty();
    }

    /**
     * Returns the set of interfaces to implement (mutable).
     *
     * @return implementsFrom set
     */
    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    /**
     * Builds the Message instance.
     *
     * @return a new Message with all configured properties
     * @throws IllegalStateException if required fields are missing
     */
    public Message build() {
        validateRequiredFields();

        Message message = new Message();
        message.setMessageName(messageName);
        message.setMessagePackageName(messagePackageName);

        if (summary != null) message.setSummary(summary);
        if (fillParameters != null) message.setFillParameters(fillParameters);
        if (commonPackageName != null) message.setCommonPackageName(commonPackageName);
        if (lombokProperties != null) message.setLombokProperties(lombokProperties);
        if (extendsFrom != null) message.setExtendsFrom(extendsFrom);
        if (!implementsFrom.isEmpty()) message.getImplementsFrom().addAll(implementsFrom);
        if (!importSet.isEmpty()) message.getImportSet().addAll(importSet);
        if (!classAnnotations.isEmpty()) message.getClassAnnotations().addAll(classAnnotations);
        if (pathForGenerateMessage != null) message.setPathForGenerateMessage(pathForGenerateMessage);

        return message;
    }

    private void validateRequiredFields() {
        if (messageName == null || messageName.trim().isEmpty()) {
            throw new IllegalStateException("messageName is required");
        }
        if (messagePackageName == null || messagePackageName.trim().isEmpty()) {
            throw new IllegalStateException("messagePackageName is required");
        }
    }
}
