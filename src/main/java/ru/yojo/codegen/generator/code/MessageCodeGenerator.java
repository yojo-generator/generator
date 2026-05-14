package ru.yojo.codegen.generator.code;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.BuilderProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

/**
 * Generates Java source code for a {@link Message} object.
 * Handles:
 * <ul>
 *   <li>Class-level JavaDoc (from {@code summary})</li>
 *   <li>Lombok annotations (if enabled)</li>
 *   <li>Inheritance/implementation clauses</li>
 *   <li>Fields (via {@link FillParameters#toWrite()})</li>
 *   <li>Getters/setters (if Lombok disabled)</li>
 *   <li>Package and imports</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class MessageCodeGenerator extends AbstractCodeGenerator {

    private final Message message;

    /**
     * Creates a generator for the given message definition.
     *
     * @param message the message to generate Java source for
     */
    public MessageCodeGenerator(Message message) {
        this.message = message;
    }

    /**
     * Generates the full Java source code for the message DTO.
     *
     * @return complete Java source for the message class
     */
    public String generate() {
        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();
        
        StringBuilder stringBuilder = prepareStringBuilder(
                requiredImports,
                message.getImplementsFrom(),
                message.getExtendsFrom(),
                message.getMessageName(),
                message.getImportSet(),
                message.getFillParameters()
        );

        LombokProperties lombokProperties = message.getLombokProperties();

        // Identify final fields without default values — they need constructor initialization
        List<VariableProperties> finalFieldsWithoutDefaults = new ArrayList<>();
        for (VariableProperties vp : message.getFillParameters().getVariableProperties()) {
            if (vp.isFinal() && vp.getDefaultProperty() == null && vp.getRealisation() == null) {
                finalFieldsWithoutDefaults.add(vp);
            }
        }
        boolean hasUninitializedFinalFields = !finalFieldsWithoutDefaults.isEmpty();

        if (lombokProperties != null && lombokProperties.enableLombok()) {
            if (message.getFillParameters().getVariableProperties().stream()
                    .anyMatch(prop -> "Data".equals(prop.getType()))) {
                lombokAnnotationBuilder
                        .append(LOMBOK_DATA_ANNOTATION.replace("@", "@lombok."))
                        .append(lineSeparator());
            } else {
                if (!message.getFillParameters().getVariableProperties().isEmpty()) {
                    lombokAnnotationBuilder
                            .append(LOMBOK_DATA_ANNOTATION)
                            .append(lineSeparator());
                    requiredImports.add(LOMBOK_DATA_IMPORT);
                } else {
                    lombokProperties.setAllArgsConstructor(false);
                }
            }
            
            // Handle Lombok properties from FillParameters
            if (message.getFillParameters().getLombokProperties() != null) {
                if (message.getFillParameters().getLombokProperties().getAccessors() != null) {
                    lombokProperties.setAccessors(message.getFillParameters().getLombokProperties().getAccessors());
                }
            }

            // @NoArgsConstructor fails with uninitialized final fields — skip it
            if (hasUninitializedFinalFields && lombokProperties.noArgsConstructor()) {
                LombokProperties modifiedLombok = LombokProperties.newLombokProperties(lombokProperties);
                modifiedLombok.setNoArgsConstructor(false);
                buildLombokAnnotations(modifiedLombok, requiredImports, lombokAnnotationBuilder);
            } else {
                buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
            }
        }

        // Apply @Singular and @Builder.Default to fields before generating declarations
        // NOTE: These Lombok annotations only apply when Lombok is enabled.
        // When Lombok is disabled, the manual builder class handles the builder pattern.
        BuilderProperties builderProps = lombokProperties != null ? lombokProperties.getBuilder() : null;
        boolean lombokEnabled = lombokProperties != null && lombokProperties.enableLombok();
        if (builderProps != null && builderProps.isEnable() && lombokEnabled) {
            for (VariableProperties vp : message.getFillParameters().getVariableProperties()) {
                // @Singular for collection fields (List, Set)
                if (builderProps.isSingular() && isCollectionType(vp.getType())) {
                    String singularName = deriveSingularName(vp.getName());
                    vp.getAnnotationSet().add(String.format(LOMBOK_SINGULAR_ANNOTATION, singularName));
                }
                // @Builder.Default for fields with default values
                if (builderProps.isBuilderDefault() && vp.getDefaultProperty() != null) {
                    vp.getAnnotationSet().add(LOMBOK_BUILDER_DEFAULT_ANNOTATION);
                }
            }
        }

        // Add field declarations
        String fields = message.getFillParameters().toWrite();
        if (fields != null && !fields.isEmpty()) {
            stringBuilder.append(fields);
        }

        // For without-Lombok: generate constructor for uninitialized final fields
        if (!finalFieldsWithoutDefaults.isEmpty() && (lombokProperties == null || !lombokProperties.enableLombok())) {
            stringBuilder
                    .append(lineSeparator())
                    .append(generateConstructor(message.getMessageName(), finalFieldsWithoutDefaults))
                    .append(lineSeparator());
        }

        if (lombokProperties == null || !lombokProperties.enableLombok()) {
            message.getFillParameters().getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                if (reference != null && vp.getEnumeration() == null) {
                    if (!vp.isFinal()) {
                        stringBuilder
                                .append(lineSeparator())
                                .append(generateSetter(reference, uncapitalize(reference)))
                                .append(lineSeparator());
                    }
                    stringBuilder.append(generateGetter(reference, uncapitalize(reference)));
                } else if (vp.getEnumeration() == null) {
                    // Regular field (not enum, not reference)
                    if (!vp.isFinal()) {
                        stringBuilder
                                .append(lineSeparator())
                                .append(generateSetter(vp.getType(), vp.getName()))
                                .append(lineSeparator());
                    }
                    stringBuilder.append(generateGetter(vp.getType(), vp.getName()));
                } else {
                    message.getFillParameters().getVariableProperties().stream()
                            .filter(varProp -> vp.equals(varProp))
                            .flatMap(variableProperties -> {
                                Set<String> i = variableProperties.getRequiredImports();
                                if (variableProperties.isEnum()) {
                                    stringBuilder
                                            .append(lineSeparator())
                                            .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                            .append(lineSeparator())
                                            .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                                }
                                return i.stream();
                            })
                            .forEach(requiredImports::add);
                }
            });

            // Manual builder class (without-Lombok path)
            if (builderProps != null && builderProps.isEnable()) {
                generateManualBuilder(message.getMessageName(),
                        message.getFillParameters().getVariableProperties(),
                        requiredImports, stringBuilder);
            }
        }

        // Add class-level annotations
        Set<String> classAnnotations = message.getClassAnnotations();
        if (classAnnotations != null && !classAnnotations.isEmpty()) {
            for (String annotation : classAnnotations) {
                lombokAnnotationBuilder.append("@").append(annotation).append(lineSeparator());
                requiredImports.add(annotation.endsWith(";") ? annotation : annotation + ";");
            }
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        message.getFillParameters().getVariableProperties().forEach(vp -> requiredImports.addAll(vp.getRequiredImports()));

        return finishBuild(stringBuilder, requiredImports, message.getMessagePackageName(), message.getSummary()).toString();
    }

    /**
     * Uncapitalizes first letter of a string.
     */
    private static String uncapitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
