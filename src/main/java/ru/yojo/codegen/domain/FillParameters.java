package ru.yojo.codegen.domain;

import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.*;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Container for a set of fields and associated metadata (e.g., Lombok config, validation groups).
 * <p>
 * Used by {@link ru.yojo.codegen.domain.schema.Schema} and {@link ru.yojo.codegen.domain.message.Message}
 * to hold all field definitions and generation settings.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
@SuppressWarnings("all")
public class FillParameters {

    /**
     * List of all field definitions (including enums, collections, maps, etc.).
     */
    private List<VariableProperties> variableProperties = new LinkedList<>();

    /**
     * Validation group classes for conditional validation (e.g., {@code ApplicationValidation.Create.class}).
     * Used with {@code groups = {...}} in validation annotations.
     */
    private Set<String> validationGroups = new HashSet<>();

    /**
     * Import declarations for validation group classes.
     * Example: {@code "ru.yojo.codegen.validation.ApplicationValidation"}.
     */
    private Set<String> validationGroupsImports = new HashSet<>();

    /**
     * Lombok configuration to apply to the containing class.
     */
    private LombokProperties lombokProperties;

    /**
     * Constructs an empty container.
     */
    public FillParameters() {
    }

    /**
     * Returns the Lombok configuration.
     *
     * @return Lombok settings or {@code null} if not set
     */
    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

    /**
     * Sets the Lombok configuration.
     *
     * @param lombokProperties Lombok config
     */
    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    /**
     * Returns the list of validation group class names (e.g., {@code "Create.class"}).
     *
     * @return set of group identifiers
     */
    public Set<String> getValidationGroups() {
        return validationGroups;
    }

    /**
     * Sets the validation group class names.
     *
     * @param validationGroups group identifiers
     */
    public void setValidationGroups(Set<String> validationGroups) {
        this.validationGroups = validationGroups;
    }

    /**
     * Returns import declarations for validation group classes.
     *
     * @return set of imports (e.g., {@code "com.example.ValidationGroups"})
     */
    public Set<String> getValidationGroupsImports() {
        return validationGroupsImports;
    }

    /**
     * Sets import declarations for validation group classes.
     *
     * @param validationGroupsImports import strings
     */
    public void setValidationGroupsImports(Set<String> validationGroupsImports) {
        this.validationGroupsImports = validationGroupsImports;
    }

    /**
     * Constructs a container with a predefined list of fields.
     *
     * @param variableProperties field definitions
     */
    public FillParameters(List<VariableProperties> variableProperties) {
        this.variableProperties = variableProperties;
    }

    /**
     * Returns the list of all field definitions.
     *
     * @return list of {@link VariableProperties}
     */
    public List<VariableProperties> getVariableProperties() {
        return variableProperties;
    }

    /**
     * Sets the list of field definitions.
     *
     * @param schemaVariableProperties fields to use
     */
    public void setVariableProperties(List<VariableProperties> schemaVariableProperties) {
        this.variableProperties = schemaVariableProperties;
    }

    /**
     * Generates Java source code for all fields in this container.
     * <p>
     * Handles two modes:
     * <ul>
     *   <li><b>Enum mode</b> — when all properties represent enum constants (e.g., {@code SUCCESS, ERROR;})</li>
     *   <li><b>Field mode</b> — regular DTO fields with types, annotations, and optional default values</li>
     * </ul>
     *
     * @return Java source code fragment containing all fields (without class wrapper)
     */
    public String toWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        if (variableProperties.stream().anyMatch(vp -> vp.getEnumeration() != null)) {
            // Enum constant declaration block (e.g., SUCCESS("desc"), ERROR;)
            stringBuilder.append(lineSeparator());
            for (int i = 0; i < variableProperties.size(); i++) {
                VariableProperties vp = variableProperties.get(i);
                if (JAVA_DEFAULT_TYPES.contains(vp.getType())) {
                    // Fallback: not an enum constant — treat as normal field (e.g., in hybrid schemas)
                    Comparator<String> stringComparator = (a, b) -> Integer.compare(a.length(), b.length());
                    vp.getAnnotationSet().stream().sorted().sorted(stringComparator).forEach(annotation -> {
                        stringBuilder.append(lineSeparator())
                                .append(TABULATION)
                                .append(annotation);
                    });
                    stringBuilder.append(lineSeparator())
                            .append(String.format(FIELD, vp.getType(), vp.getName()));
                } else if (variableProperties.stream().anyMatch(v -> v.getEnumNames() != null)) {
                    // Enum with description: CONSTANT("description")
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION)
                            .append(vp.getType())
                            .append(i == variableProperties.size() - 2 ? ";" : ",")
                            .append(i == variableProperties.size() - 2 ? lineSeparator() : "");
                } else {
                    // Plain enum: CONSTANT
                    String constName = vp.getEnumeration() != null
                                       && Character.isLowerCase(vp.getOriginalEnumName().charAt(0))
                            ? vp.getOriginalEnumName()
                            : vp.getType();
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION)
                            .append(constName)
                            .append(i == variableProperties.size() - 1 ? ";" : ",");
                }
            }
            return stringBuilder.toString();
        } else {
            // Regular fields mode
            variableProperties.forEach(vp ->
                    stringBuilder
                            .append(lineSeparator())
                            .append(vp.toWrite()));
            return stringBuilder.toString();
        }
    }
}