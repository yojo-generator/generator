package ru.yojo.codegen.generator.code;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.schema.Schema;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Generates Java source code for a {@link Schema} object.
 * Handles three cases:
 * <ul>
 *   <li>Interface (marker or with method definitions)</li>
 *   <li>Enum (with or without descriptions)</li>
 *   <li>Class (regular DTO with fields, Lombok, validation)</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SchemaCodeGenerator extends AbstractCodeGenerator {

    private final Schema schema;

    /**
     * Creates a generator for the given schema definition.
     *
     * @param schema the schema to generate Java source for
     */
    public SchemaCodeGenerator(Schema schema) {
        this.schema = schema;
    }

    /**
     * Generates the full Java source code for this schema.
     *
     * @return complete Java source code
     */
    public String generate() {
        StringBuilder stringBuilder;
        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();

        if (schema.isInterface()) {
            stringBuilder = generateInterface();
        } else {
            boolean hasNonEnumProperties = schema.getFillParameters().getVariableProperties().stream()
                    .anyMatch(vp -> vp.getEnumeration() == null);

            if (hasNonEnumProperties) {
                // Regular class
                stringBuilder = prepareStringBuilder(
                        requiredImports,
                        schema.getImplementsFrom(),
                        schema.getExtendsFrom(),
                        schema.getSchemaName(),
                        schema.getImportSet(),
                        schema.getFillParameters()
                );

                LombokProperties lombokProperties = schema.getLombokProperties();
                if (lombokProperties != null && lombokProperties.enableLombok()) {
                    if (schema.getSchemaName().equals("Data") ||
                            schema.getFillParameters().getVariableProperties().stream()
                                    .anyMatch(prop -> "Data".equals(prop.getType()))) {
                        lombokAnnotationBuilder
                                .append(LOMBOK_DATA_ANNOTATION.replace("@", "@lombok."))
                                .append(lineSeparator());
                    } else {
                        if (!schema.getFillParameters().getVariableProperties().isEmpty()) {
                            lombokAnnotationBuilder
                                    .append(LOMBOK_DATA_ANNOTATION)
                                    .append(lineSeparator());
                            requiredImports.add(LOMBOK_DATA_IMPORT);
                        }
                    }
                    buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
                }

                // Add field declarations
                String fields = schema.getFillParameters().toWrite();
                if (fields != null && !fields.isEmpty()) {
                    stringBuilder.append(fields);
                }

                StringBuilder finalStringBuilder = stringBuilder;
                schema.getFillParameters().getVariableProperties().stream()
                        .flatMap(variableProperties -> {
                            Set<String> i = variableProperties.getRequiredImports();
                            if (lombokProperties == null || !lombokProperties.enableLombok()) {
                                if (variableProperties.getEnumeration() == null) {
                                    finalStringBuilder
                                            .append(lineSeparator())
                                            .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                            .append(lineSeparator())
                                            .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                                }
                            }
                            return i.stream();
                        })
                        .forEach(requiredImports::add);
            } else {
                // ENUM
                stringBuilder = getEnumClassBuilder(schema.getSchemaName());

                // Remove @Valid from enums
                schema.getFillParameters().getVariableProperties().forEach(vp -> {
                    vp.getRequiredImports().remove(JAVAX_VALID_IMPORT);
                    vp.getRequiredImports().remove(JAKARTA_VALID_IMPORT);
                });

                boolean hasEnumWithDescription = schema.getFillParameters().getVariableProperties().stream()
                        .anyMatch(vp -> vp.getEnumNames() != null);
                boolean hasEnumValues = schema.getFillParameters().getVariableProperties().stream()
                        .anyMatch(vp -> vp.getEnumValues() != null);

                if (hasEnumValues) {
                    // ─── Enum with x-enumValues: @JsonValue/@JsonCreator support ─── //
                    // 1️⃣ Generate enum constants: ACTIVE("A"), ...
                    StringBuilder constantsBuilder = new StringBuilder();
                    for (int i = 0; i < schema.getFillParameters().getVariableProperties().size(); i++) {
                        VariableProperties vp = schema.getFillParameters().getVariableProperties().get(i);
                        if (vp.getEnumeration() != null) {
                            String name = vp.getEnumeration();
                            String wireValue = vp.getEnumValues() != null ? esc(vp.getEnumValues()) : "";
                            constantsBuilder
                                    .append(TABULATION)
                                    .append(name)
                                    .append("(\"")
                                    .append(wireValue)
                                    .append("\")");
                            if (i < schema.getFillParameters().getVariableProperties().size() - 1) {
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
                            .append(schema.getSchemaName())
                            .append("(String value) {")
                            .append(lineSeparator())
                            .append("        this.value = value;")
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());

                    // 3️⃣ @JsonValue on getter
                    stringBuilder
                            .append(lineSeparator())
                            .append("    @JsonValue")
                            .append(lineSeparator())
                            .append("    public String getValue() {")
                            .append(lineSeparator())
                            .append("        return value;")
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());
                    requiredImports.add(JSON_VALUE_IMPORT);

                    // 4️⃣ @JsonCreator static fromValue method (with UNKNOWN_DEFAULT_YOJO fallback)
                    stringBuilder
                            .append(lineSeparator())
                            .append("    @JsonCreator")
                            .append(lineSeparator())
                            .append("    public static ")
                            .append(schema.getSchemaName())
                            .append(" fromValue(String value) {")
                            .append(lineSeparator())
                            .append("        for (")
                            .append(schema.getSchemaName())
                            .append(" v : ")
                            .append(schema.getSchemaName())
                            .append(".values()) {")
                            .append(lineSeparator())
                            .append("            if (v.value.equals(value)) {")
                            .append(lineSeparator())
                            .append("                return v;")
                            .append(lineSeparator())
                            .append("            }")
                            .append(lineSeparator())
                            .append("        }");
                    if (schema.isEnumDefault()) {
                        stringBuilder
                                .append(lineSeparator())
                                .append("        return UNKNOWN_DEFAULT_YOJO;");
                    } else {
                        stringBuilder
                                .append(lineSeparator())
                                .append("        throw new IllegalArgumentException(\"Unknown enum value: \" + value);");
                    }
                    stringBuilder
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());
                    requiredImports.add(JSON_CREATOR_IMPORT);

                } else if (hasEnumWithDescription) {
                    // ─── Enum with x-enumNames: human-readable description ─── //
                    // 1️⃣ Generate enum constants: SUCCESS("Success value"), ...
                    StringBuilder constantsBuilder = new StringBuilder();
                    for (int i = 0; i < schema.getFillParameters().getVariableProperties().size(); i++) {
                        VariableProperties vp = schema.getFillParameters().getVariableProperties().get(i);
                        if (vp.getEnumeration() != null) {
                            String name = vp.getEnumeration();
                            String desc = vp.getEnumNames() != null ? esc(vp.getEnumNames()) : "";
                            constantsBuilder
                                    .append(TABULATION)
                                    .append(name)
                                    .append("(\"")
                                    .append(desc)
                                    .append("\")");
                            if (i < schema.getFillParameters().getVariableProperties().size() - 1) {
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
                            .append(schema.getSchemaName())
                            .append("(String value) {")
                            .append(lineSeparator())
                            .append("        this.value = value;")
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());

                    // 3️⃣ Add @Getter or manual getter for description field
                    LombokProperties lombokProperties = schema.getLombokProperties();
                    if (lombokProperties != null && lombokProperties.enableLombok()) {
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
                    // Plain enum (no descriptions, no wire values)
                    stringBuilder.append(schema.getFillParameters().toWrite()).append(lineSeparator());
                }

                // 4️⃣ Lombok (exclude @NoArgsConstructor for enums with constructors)
                LombokProperties lombokProperties = schema.getLombokProperties();
                if (lombokProperties != null && lombokProperties.enableLombok()) {
                    LombokProperties effectiveLombok = LombokProperties.newLombokProperties(lombokProperties);
                    effectiveLombok.setNoArgsConstructor(false);
                    effectiveLombok.setAllArgsConstructor(false);
                    buildLombokAnnotations(effectiveLombok, requiredImports, lombokAnnotationBuilder);
                }
            }
        }

        // Add class-level annotations
        Set<String> classAnnotations = schema.getClassAnnotations();
        if (classAnnotations != null && !classAnnotations.isEmpty()) {
            for (String annotation : classAnnotations) {
                String simpleName = annotation.contains(".")
                        ? annotation.substring(annotation.lastIndexOf('.') + 1)
                        : annotation;
                lombokAnnotationBuilder.append("@").append(simpleName).append(lineSeparator());
                requiredImports.add(annotation.endsWith(";") ? annotation : annotation + ";");
            }
        }

        // Add Jackson polymorphic annotations (discriminator)
        String discriminator = schema.getDiscriminator();
        if (discriminator != null && !discriminator.isEmpty()) {
            requiredImports.add(JSON_TYPE_INFO_IMPORT);
            requiredImports.add(JSON_SUB_TYPES_IMPORT);
            lombokAnnotationBuilder.append(String.format(JSON_TYPE_INFO_ANNOTATION, discriminator)).append(lineSeparator());

            if (schema.getSubtypes() != null && !schema.getSubtypes().isEmpty()) {
                StringBuilder subtypesBuilder = new StringBuilder();
                subtypesBuilder.append("@JsonSubTypes({").append(lineSeparator());
                for (int i = 0; i < schema.getSubtypes().size(); i++) {
                    String subtype = schema.getSubtypes().get(i);
                    String discriminatorValue = schema.getSubtypeDiscriminatorValue(subtype);
                    subtypesBuilder.append(String.format("    @JsonSubTypes.Type(value = %s.class, name = \"%s\")", subtype, discriminatorValue));
                    if (i < schema.getSubtypes().size() - 1) {
                        subtypesBuilder.append(",").append(lineSeparator());
                    } else {
                        subtypesBuilder.append(lineSeparator());
                    }
                }
                subtypesBuilder.append("})");
                lombokAnnotationBuilder.append(subtypesBuilder).append(lineSeparator());
            }
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        return finishBuild(stringBuilder, requiredImports, schema.getPackageName(), schema.getDescription()).toString();
    }

    /**
     * Generates Java source for an interface (marker or with methods).
     *
     * @return interface source code
     */
    private StringBuilder generateInterface() {
        StringBuilder stringBuilder = getInterfaceBuilder(schema.getSchemaName());
        Map<String, Object> methods = schema.getMethods();
        if (methods != null && !methods.isEmpty()) {
            methods.values().forEach(method -> {
                Map<String, Object> currentMethod = castObjectToMap(method);
                String methodDescription = getStringValueIfExistOrElseNull(DESCRIPTION, currentMethod);
                String methodDefinition = getStringValueIfExistOrElseNull(DEFINITION, currentMethod);
                if (methodDescription != null) {
                    generateClassJavaDoc(stringBuilder, methodDescription);
                }
                methodDefinition = TABULATION.concat(methodDefinition.endsWith(";") ? methodDefinition : methodDefinition + ";");
                stringBuilder
                        .append(lineSeparator())
                        .append(methodDefinition)
                        .append(lineSeparator());
            });
        }
        Set<String> interfaceImports = schema.getInterfaceImports();
        if (interfaceImports != null && !interfaceImports.isEmpty()) {
            schema.getImportSet().addAll(interfaceImports);
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
