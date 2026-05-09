package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a generated Java class, enum, or interface from an AsyncAPI {@code components.schemas.*} definition.
 * <p>
 * Supports:
 * <ul>
 *   <li>Regular DTO classes (with fields, Lombok, validation)</li>
 *   <li>Enums (with/without human-readable descriptions)</li>
 *   <li>Marker interfaces or interfaces with method definitions</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class Schema {

    /**
     * Name of the schema (e.g., {@code "User"}), used as Java class name.
     */
    private String schemaName;

    /**
     * Schema description (e.g., from {@code description} field in YAML); used in class-level JavaDoc.
     */
    private String description;

    /**
     * Target Java package (e.g., {@code "com.example.api.common;"}).
     */
    private String packageName;

    /**
     * Lombok configuration to apply to this schema.
     */
    private LombokProperties lombokProperties;

    /**
     * Contains all fields, validation groups, and Lombok settings for this schema.
     */
    private FillParameters fillParameters;

    /**
     * Superclass to extend (e.g., {@code "BaseEntity"}), or {@code null}.
     */
    private String extendsFrom;

    /**
     * Interfaces to implement (e.g., {@code ["Serializable"]}).
     * Values are simple names (not fully qualified); imports are tracked separately.
     */
    private Set<String> implementsFrom = new HashSet<>();

    /**
     * Custom import declarations (e.g., for manually specified interfaces or external types).
     */
    private Set<String> importSet = new HashSet<>();

    /**
     * {@code true} if this schema should be generated as a Java {@code interface}.
     */
    private boolean isInterface;

    /**
     * Method definitions (used only for interfaces).
     * Key — method name; value — map with {@code description} and {@code definition}.
     */
    private Map<String, Object> methods = new LinkedHashMap<>();

    /**
     * Import declarations for interfaces (e.g., types used in method signatures).
     */
    private Set<String> interfaceImports = new HashSet<>();

    /**
     * Class-level annotations specified via x-class-annotation.
     */
    private Set<String> classAnnotations = new HashSet<>();

    /**
     * Discriminator field name for polymorphic serialization.
     */
    private String discriminator;

    /**
     * Discriminator field name in subtypes (the field that should get @JsonTypeId).
     */
    private String discriminatorField;

    /**
     * List of subtypes for @JsonSubTypes annotation.
     */
    private List<String> subtypes = new ArrayList<>();

    /**
     * Set of unique subtypes (deduplicated).
     */
    private Set<String> uniqueSubtypes = new HashSet<>();

    /**
     * Map from subtype schema name to discriminator value (for @JsonSubTypes.Type name = "...").
     * If a subtype has a const value in the discriminator field, that value is used instead of schema name.
     */
    private Map<String, String> subtypeDiscriminatorValues = new LinkedHashMap<>();

    /**
     * Adds a subtype with default discriminator value (schema name).
     */
    public void addSubtype(String subtype) {
        addSubtype(subtype, subtype);
    }

    /**
     * Adds a subtype with explicit discriminator value.
     *
     * @param subtype           schema name of the subtype
     * @param discriminatorValue value for @JsonSubTypes.Type(name = "...")
     */
    public void addSubtype(String subtype, String discriminatorValue) {
        if (!uniqueSubtypes.contains(subtype)) {
            uniqueSubtypes.add(subtype);
            subtypes.add(subtype);
            subtypeDiscriminatorValues.put(subtype, discriminatorValue);
        }
    }

    /**
     * Returns the discriminator value for a given subtype.
     *
     * @param subtype schema name
     * @return discriminator value (defaults to subtype name if not explicitly set)
     */
    public String getSubtypeDiscriminatorValue(String subtype) {
        return subtypeDiscriminatorValues.getOrDefault(subtype, subtype);
    }

    /**
     * Clears all subtypes.
     */
    public void clearSubtypes() {
        subtypes.clear();
        uniqueSubtypes.clear();
        subtypeDiscriminatorValues.clear();
    }

    // —— Getters & Setters —— //

    /**
     * Returns the class name (e.g., {@code "User"}).
     *
     * @return schema name
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets the class name.
     *
     * @param schemaName class name (simple, not qualified)
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Returns the schema description (used in class JavaDoc).
     *
     * @return description or {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the schema description.
     *
     * @param description description text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the Lombok configuration.
     *
     * @param lombokProperties Lombok settings
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    /**
     * Returns the Lombok configuration.
     *
     * @return Lombok settings or {@code null}
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Sets the field and validation container.
     *
     * @param fillParameters field definitions and metadata
     */
    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    /**
     * Returns the field and validation container.
     *
     * @return fill parameters with all fields
     */
    public FillParameters getFillParameters() {
        return fillParameters;
    }

    /**
     * Sets the target Java package.
     *
     * @param packageName full package with trailing semicolon (e.g., {@code "com.example.common;"})
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Returns the target Java package.
     *
     * @return package name with trailing semicolon (e.g., {@code "com.example.common;"})
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the superclass to extend.
     *
     * @param extendsFrom superclass name (simple, not qualified)
     */
    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    /**
     * Returns the superclass to extend (or {@code null} if none).
     *
     * @return superclass name
     */
    public String getExtendsFrom() {
        return extendsFrom;
    }

    /**
     * Returns the set of interfaces to implement.
     *
     * @return interface simple names
     */
    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    /**
     * Returns custom import declarations.
     *
     * @return imports (e.g., {@code "com.example.SomeType;"})
     */
    public Set<String> getImportSet() {
        return importSet;
    }

    /**
     * Returns whether this schema is an interface.
     *
     * @return {@code true} for interfaces
     */
    public boolean isInterface() {
        return isInterface;
    }

    /**
     * Sets whether this schema is an interface.
     *
     * @param anInterface {@code true} to generate as interface
     */
    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    /**
     * Returns interface-level imports (e.g., types referenced in method signatures).
     *
     * @return import strings
     */
    public Set<String> getInterfaceImports() {
        return interfaceImports;
    }

    /**
     * Sets interface-level imports.
     *
     * @param interfaceImports imports for interface methods
     */
    public void setInterfaceImports(Set<String> interfaceImports) {
        this.interfaceImports = interfaceImports;
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

    /**
     * Returns the discriminator field name.
     */
    public String getDiscriminator() {
        return discriminator;
    }

    /**
     * Sets the discriminator field name.
     */
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    /**
     * Returns the discriminator field name for subtypes (the field to annotate with @JsonTypeId).
     */
    public String getDiscriminatorField() {
        return discriminatorField;
    }

    /**
     * Sets the discriminator field name for subtypes.
     */
    public void setDiscriminatorField(String discriminatorField) {
        this.discriminatorField = discriminatorField;
    }

    /**
     * Returns list of subtypes.
     */
    public List<String> getSubtypes() {
        return subtypes;
    }

    /**
     * Sets list of subtypes.
     */
    public void setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
    }

    /**
     * Returns set of unique subtypes.
     */
    public Set<String> getUniqueSubtypes() {
        return uniqueSubtypes;
    }

    /**
     * Returns map of subtype discriminator values.
     */
    public Map<String, String> getSubtypeDiscriminatorValues() {
        return subtypeDiscriminatorValues;
    }

    /**
     * Returns method definitions (for interfaces only).
     *
     * @return method map: name → { description, definition }
     */
    public Map<String, Object> getMethods() {
        return methods;
    }

    /**
     * Sets method definitions.
     *
     * @param methods method map
     */
    public void setMethods(Map<String, Object> methods) {
        this.methods = methods;
    }

}
