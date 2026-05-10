package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Builder for {@link Schema} objects.
 * Provides fluent API to construct Schema instances with required validation.
 *
 * <p>Example usage:
 * <pre>{@code
 * Schema schema = new SchemaBuilder()
 *     .name("User")
 *     .packageName("com.example.common;")
 *     .lombokProperties(lombokProps)
 *     .addField(variableProperties)
 *     .build();
 * }</pre>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SchemaBuilder {

    // Required fields
    private String schemaName;
    private String packageName;

    // Optional fields
    private String description;
    private LombokProperties lombokProperties;
    private FillParameters fillParameters;
    private String extendsFrom;
    private Set<String> implementsFrom = new HashSet<>();
    private Set<String> importSet = new HashSet<>();
    private boolean isInterface = false;
    private Map<String, Object> methods = new LinkedHashMap<>();
    private Set<String> interfaceImports = new HashSet<>();
    private Set<String> classAnnotations = new HashSet<>();
    private String discriminator;
    private String discriminatorField;

    /**
     * Sets the schema name (required).
     *
     * @param schemaName class name (e.g., "User")
     * @return this builder
     */
    public SchemaBuilder name(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    /**
     * Sets the target Java package (required).
     *
     * @param packageName full package with trailing semicolon (e.g., "com.example.common;")
     * @return this builder
     */
    public SchemaBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * Sets the schema description (optional).
     *
     * @param description description text
     * @return this builder
     */
    public SchemaBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the Lombok configuration (optional).
     *
     * @param lombokProperties Lombok settings
     * @return this builder
     */
    public SchemaBuilder lombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
        return this;
    }

    /**
     * Sets the field and validation container (optional).
     *
     * @param fillParameters field definitions
     * @return this builder
     */
    public SchemaBuilder fillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
        return this;
    }

    /**
     * Sets the superclass to extend (optional).
     *
     * @param extendsFrom superclass name
     * @return this builder
     */
    public SchemaBuilder extendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
        return this;
    }

    /**
     * Adds an interface to implement (optional).
     *
     * @param interfaceName interface name
     * @return this builder
     */
    public SchemaBuilder addImplementsFrom(String interfaceName) {
        this.implementsFrom.add(interfaceName);
        return this;
    }

    /**
     * Sets whether this schema should be generated as an interface (optional).
     *
     * @param isInterface true if interface
     * @return this builder
     */
    public SchemaBuilder isInterface(boolean isInterface) {
        this.isInterface = isInterface;
        return this;
    }

    /**
     * Adds a method definition (for interfaces).
     *
     * @param methodName method name
     * @param methodDef  method definition map
     * @return this builder
     */
    public SchemaBuilder addMethod(String methodName, Map<String, Object> methodDef) {
        this.methods.put(methodName, methodDef);
        return this;
    }

    /**
     * Sets the discriminator field name (optional).
     *
     * @param discriminator discriminator field
     * @return this builder
     */
    public SchemaBuilder discriminator(String discriminator) {
        this.discriminator = discriminator;
        return this;
    }

    /**
     * Sets the discriminator field name for subtypes (@JsonTypeId target) (optional).
     *
     * @param discriminatorField discriminator field for subtypes
     * @return this builder
     */
    public SchemaBuilder discriminatorField(String discriminatorField) {
        this.discriminatorField = discriminatorField;
        return this;
    }

    /**
     * Sets method definitions for interfaces (optional).
     * Replaces any previously added methods.
     *
     * @param methods method map: name {@literal ->} { description, definition }
     * @return this builder
     */
    public SchemaBuilder methods(Map<String, Object> methods) {
        this.methods = new LinkedHashMap<>(methods);
        return this;
    }

    /**
     * Sets interface-level imports (optional).
     *
     * @param interfaceImports set of import strings for interface methods
     * @return this builder
     */
    public SchemaBuilder interfaceImports(Set<String> interfaceImports) {
        this.interfaceImports = new HashSet<>(interfaceImports);
        return this;
    }

    /**
     * Adds a custom import declaration (optional).
     *
     * @param importStmt import statement (e.g., "com.example.SomeType;")
     * @return this builder
     */
    public SchemaBuilder addImport(String importStmt) {
        this.importSet.add(importStmt);
        return this;
    }

    /**
     * Adds a class-level annotation (optional).
     *
     * @param annotation annotation string
     * @return this builder
     */
    public SchemaBuilder addClassAnnotation(String annotation) {
        this.classAnnotations.add(annotation);
        return this;
    }

    /**
     * Builds the Schema instance.
     *
     * @return a new Schema with all configured properties
     * @throws IllegalStateException if required fields are missing
     */
    public Schema build() {
        validateRequiredFields();

        Schema schema = new Schema();
        schema.setSchemaName(schemaName);
        schema.setPackageName(packageName);

        if (description != null) schema.setDescription(description);
        if (lombokProperties != null) schema.setLombokProperties(lombokProperties);
        if (fillParameters != null) schema.setFillParameters(fillParameters);
        if (extendsFrom != null) schema.setExtendsFrom(extendsFrom);
        if (!implementsFrom.isEmpty()) schema.getImplementsFrom().addAll(implementsFrom);
        if (!importSet.isEmpty()) schema.getImportSet().addAll(importSet);
        schema.setInterface(isInterface);
        if (!methods.isEmpty()) schema.getMethods().putAll(methods);
        if (!interfaceImports.isEmpty()) schema.getInterfaceImports().addAll(interfaceImports);
        if (!classAnnotations.isEmpty()) schema.getClassAnnotations().addAll(classAnnotations);
        if (discriminator != null) schema.setDiscriminator(discriminator);
        if (discriminatorField != null) {
            schema.setDiscriminatorField(discriminatorField);
            // Automatically mark the matching VariableProperty as discriminator field (for @JsonTypeId)
            if (fillParameters != null) {
                for (VariableProperties vp : fillParameters.getVariableProperties()) {
                    if (vp.getName() != null && vp.getName().equals(discriminatorField)) {
                        vp.setDiscriminatorField(true);
                        break;
                    }
                }
            }
        }
        return schema;
    }

    private void validateRequiredFields() {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalStateException("schemaName is required");
        }
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalStateException("packageName is required");
        }
    }
}
