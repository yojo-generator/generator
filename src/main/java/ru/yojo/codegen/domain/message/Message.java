package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an AsyncAPI message (i.e., a DTO generated from a {@code components.messages.*} definition).
 * <p>
 * Generated classes are placed in the {@code .messages} package and typically contain payload fields,
 * inheritance/implementation metadata, and optional Lombok annotations.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class Message {

    /**
     * Summary from message definition (used in class-level JavaDoc).
     */
    private String summary;

    /**
     * Contains all payload fields and metadata (e.g., Lombok config, validation groups).
     */
    private FillParameters fillParameters;

    /**
     * Class name of the generated DTO (e.g., {@code "UserSignedUp"}).
     */
    private String messageName;

    /**
     * Full package for messages (e.g., {@code "com.example.api.messages;"}).
     */
    private String messagePackageName;

    /**
     * Full package for schemas/common types (e.g., {@code "com.example.api.common;"}).
     */
    private String commonPackageName;

    /**
     * Lombok configuration to apply to this message class.
     */
    private LombokProperties lombokProperties;

    /**
     * Fully qualified name of superclass to extend (e.g., {@code "BaseMessage"}).
     */
    private String extendsFrom;

    /**
     * List of interfaces to implement (e.g., {@code ["Serializable"]}).
     */
    private Set<String> implementsFrom = new HashSet<>();

    /**
     * Custom imports to add (e.g., for manually specified interfaces or external types).
     */
    private Set<String> importSet = new HashSet<>();

    /**
     * Class-level annotations specified via x-class-annotation.
     */
    private Set<String> classAnnotations = new HashSet<>();

    /**
     * Optional custom package path (e.g., {@code "io.github.somepath"}), overrides {@link #messagePackageName}.
     * Used when {@code pathForGenerateMessage} is specified in the contract.
     */
    private String pathForGenerateMessage;

    // —— Getters & Setters —— //

    /**
     * Returns the Java field name.
     *
     * @return field name
     */
    public String getName() {
        return messageName;
    }

    /**
     * Sets the Lombok configuration for this message.
     *
     * @param lombokProperties Lombok settings
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    /**
     * Returns the effective Lombok configuration.
     *
     * @return Lombok settings
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Returns the generated class name (e.g., {@code "RequestDtoByRef"}).
     *
     * @return class name
     */
    public String getMessageName() {
        return messageName;
    }

    /**
     * Sets the class name.
     *
     * @param messageName name of the generated class
     */
    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    /**
     * Sets the default message package (used if {@link #pathForGenerateMessage} is not set).
     *
     * @param messagePackageName package with trailing semicolon (e.g., {@code "com.example.messages;"})
     */
    public void setMessagePackageName(String messagePackageName) {
        this.messagePackageName = messagePackageName;
    }

    /**
     * Returns the message package name.
     *
     * @return package name with trailing semicolon (e.g., {@code "com.example.messages;"})
     */
    public String getMessagePackageName() {
        return messagePackageName;
    }

    /**
     * Sets the common schema package (used for imports of referenced DTOs/enums).
     *
     * @param commonPackageName package with trailing semicolon (e.g., {@code "com.example.common;"})
     */
    public void setCommonPackageName(String commonPackageName) {
        this.commonPackageName = commonPackageName;
    }

    /**
     * Returns the superclass name (or {@code null} if none).
     *
     * @return superclass name
     */
    public String getExtendsFrom() {
        return extendsFrom;
    }

    /**
     * Sets the superclass to extend.
     *
     * @param extendsFrom class name (simple, not fully qualified)
     */
    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    /**
     * Sets the summary text (used in class-level JavaDoc).
     *
     * @param summary summary from AsyncAPI {@code summary} field
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Returns the summary text.
     *
     * @return summary or {@code null}
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Merges fields from another {@link FillParameters} into this one.
     * Used when message combines {@code $ref} and inline {@code properties}.
     *
     * @param fillParameters source parameters to merge
     */
    public void enrichFillParameters(FillParameters fillParameters) {
        this.fillParameters.getVariableProperties().addAll(fillParameters.getVariableProperties());
    }

    /**
     * Sets the payload field definitions and metadata.
     *
     * @param fillParameters field container
     */
    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    /**
     * Returns the set of interfaces to implement.
     *
     * @return interface names (simple, not fully qualified)
     */
    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    /**
     * Returns custom import declarations (e.g., for manually added interfaces).
     *
     * @return set of import strings (e.g., {@code "com.example.SomeInterface;"})
     */
    public Set<String> getImportSet() {
        return importSet;
    }

    /**
     * Returns the custom generation path (if specified via {@code pathForGenerateMessage} in YAML).
     *
     * @return custom package path (e.g., {@code "io.github.somepath"}) or {@code null}
     */
    public String getPathForGenerateMessage() {
        return pathForGenerateMessage;
    }

    /**
     * Sets the custom generation path.
     *
     * @param pathForGenerateMessage package path without trailing semicolon
     */
    public void setPathForGenerateMessage(String pathForGenerateMessage) {
        this.pathForGenerateMessage = pathForGenerateMessage;
    }

    /**
     * Returns class-level annotations.
     *
     * @return class annotations (e.g., "com.example.MyAnnotation")
     */
    public Set<String> getClassAnnotations() {
        return classAnnotations;
    }

    /**
     * Sets class-level annotations.
     *
     * @param classAnnotations set of fully qualified annotation names
     */
    public void setClassAnnotations(Set<String> classAnnotations) {
        this.classAnnotations = classAnnotations;
    }

    public FillParameters getFillParameters() {
        return fillParameters;
    }
}
