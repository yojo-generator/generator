package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

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

    // ——— Getters & Setters ——— //

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
     * Sets the field and validation container.
     *
     * @param fillParameters field definitions and metadata
     */
    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
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
     * Sets the superclass to extend.
     *
     * @param extendsFrom superclass name (simple, not qualified)
     */
    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
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

    // ——— CORE: toWrite() ——— //

    /**
     * Generates the full Java source code for this schema.
     * <p>
     * Handles three cases:
     * <ul>
     *   <li><b>Interface</b> — marker or with method definitions</li>
     *   <li><b>Enum</b> — with or without descriptions</li>
     *   <li><b>Class</b> — regular DTO with fields, Lombok, validation</li>
     * </ul>
     *
     * @return complete Java source code
     */
    public String toWrite() {
        StringBuilder stringBuilder;
        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();

        if (isInterface) {
            stringBuilder = generateInterface();
        } else {
            boolean hasNonEnumProperties = fillParameters.getVariableProperties().stream()
                    .anyMatch(vp -> vp.getEnumeration() == null);

            if (hasNonEnumProperties) {
                // Regular class
                stringBuilder = prepareStringBuilder(
                        requiredImports,
                        implementsFrom,
                        extendsFrom,
                        schemaName,
                        importSet,
                        fillParameters
                );

                if (lombokProperties.enableLombok()) {
                    if (schemaName.equals("Data") ||
                        fillParameters.getVariableProperties().stream().anyMatch(prop -> "Data".equals(prop.getType()))) {
                        lombokAnnotationBuilder
                                .append(LOMBOK_DATA_ANNOTATION.replace("@", "@lombok."))
                                .append(lineSeparator());
                    } else {
                        lombokAnnotationBuilder
                                .append(LOMBOK_DATA_ANNOTATION)
                                .append(lineSeparator());
                        requiredImports.add(LOMBOK_DATA_IMPORT);
                    }
                    buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
                }

                StringBuilder finalStringBuilder = stringBuilder;
                fillParameters.getVariableProperties().stream()
                        .flatMap(variableProperties -> {
                            Set<String> i = variableProperties.getRequiredImports();
                            if (!lombokProperties.enableLombok() && variableProperties.getEnumeration() == null) {
                                    finalStringBuilder
                                            .append(lineSeparator())
                                            .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                            .append(lineSeparator())
                                            .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                            }
                            return i.stream();
                        })
                        .forEach(requiredImports::add);
            } else {
                // ENUM
                stringBuilder = getEnumClassBuilder(schemaName);

                // Remove @Valid from enums
                fillParameters.getVariableProperties().forEach(vp -> {
                    vp.getRequiredImports().remove(JAVAX_VALID_IMPORT);
                    vp.getRequiredImports().remove(JAKARTA_VALID_IMPORT);
                });

                boolean hasEnumWithDescription = fillParameters.getVariableProperties().stream()
                        .anyMatch(vp -> vp.getEnumNames() != null);

                if (hasEnumWithDescription) {
                    // 1️⃣ Generate enum constants: SUCCESS("Success value"), ...
                    StringBuilder constantsBuilder = new StringBuilder();
                    for (int i = 0; i < fillParameters.getVariableProperties().size(); i++) {
                        VariableProperties vp = fillParameters.getVariableProperties().get(i);
                        if (vp.getEnumeration() != null) {
                            String name = vp.getEnumeration();
                            String desc = vp.getEnumNames() != null ? esc(vp.getEnumNames()) : "";
                            constantsBuilder
                                    .append(TABULATION)
                                    .append(name)
                                    .append("(\"")
                                    .append(desc)
                                    .append("\")");
                            if (i < fillParameters.getVariableProperties().size() - 1) {
                                constantsBuilder.append(",");
                            }
                            constantsBuilder.append(lineSeparator());
                        }
                    }

                    // Trim trailing newline and append semicolon on same line
                    String constants = constantsBuilder.toString();
                    if (constants.endsWith(lineSeparator())) {
                        constants = constants.substring(0, constants.length() - lineSeparator().length());
                    }
                    constants += ";";

                    // 2️⃣ Build enum body
                    stringBuilder
                            .append(lineSeparator())
                            .append(constants)
                            .append(lineSeparator())
                            .append(lineSeparator())
                            .append("    private final String value;")
                            .append(lineSeparator())
                            .append(lineSeparator())
                            .append("    ")
                            .append(schemaName)
                            .append("(String value) {")
                            .append(lineSeparator())
                            .append("        this.value = value;")
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());

                    // 3️⃣ Add @Getter or manual getter for description field
                    if (lombokProperties.enableLombok()) {
                        lombokAnnotationBuilder.append("@Getter").append(lineSeparator());
                        requiredImports.add(LOMBOK_GETTER_IMPORT);
                    } else {
                        stringBuilder
                                .append(lineSeparator())
                                .append("    public String getValue() {")
                                .append(lineSeparator())
                                .append("        return value;")
                                .append(lineSeparator())
                                .append("    }")
                                .append(lineSeparator());
                    }

                } else {
                    // Plain enum (no descriptions)
                    stringBuilder.append(fillParameters.toWrite()).append(lineSeparator());
                }

                // 4️⃣ Lombok (exclude @NoArgsConstructor for enums with constructors)
                if (lombokProperties.enableLombok()) {
                    LombokProperties effectiveLombok = LombokProperties.newLombokProperties(lombokProperties);
                    effectiveLombok.setNoArgsConstructor(false);
                    effectiveLombok.setAllArgsConstructor(false);
                    buildLombokAnnotations(effectiveLombok, requiredImports, lombokAnnotationBuilder);
                }
            }
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        if (!isInterface) {
            generateClassJavaDoc(stringBuilder, description);
        }

        return finishBuild(stringBuilder, requiredImports, packageName);
    }

    // ——— Helper methods ——— //

    /**
     * Generates Java source for an interface (marker or with methods).
     *
     * @return interface source code
     */
    private StringBuilder generateInterface() {
        StringBuilder stringBuilder = getInterfaceBuilder(schemaName);
        generateClassJavaDoc(stringBuilder, description);
        if (!methods.isEmpty()) {
            methods.values().forEach(method -> {
                Map<String, Object> currentMethod = castObjectToMap(method);
                String methodDescription = getStringValueIfExistOrElseNull(DESCRIPTION, currentMethod);
                String methodDefinition = getStringValueIfExistOrElseNull(DEFINITION, currentMethod);
                if (methodDescription != null) {
                    generateJavaDoc(stringBuilder, methodDescription, null);
                }
                methodDefinition = TABULATION.concat(methodDefinition.endsWith(";") ? methodDefinition : methodDefinition + ";");
                stringBuilder
                        .append(lineSeparator())
                        .append(methodDefinition)
                        .append(lineSeparator());
            });
        }
        if (!interfaceImports.isEmpty()) {
            stringBuilder.insert(0, lineSeparator());
            interfaceImports.forEach(i -> {
                i = IMPORT.concat(i.endsWith(";") ? i : i + ";");
                stringBuilder.insert(0, i + lineSeparator());
            });
        }
        return stringBuilder;
    }

    /**
     * Escapes double quotes and backslashes in enum description strings.
     *
     * @param s input string
     * @return escaped string suitable for Java string literal
     */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}