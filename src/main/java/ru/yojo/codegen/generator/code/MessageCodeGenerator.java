package ru.yojo.codegen.generator.code;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.message.Message;

import java.util.HashSet;
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
            buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
        }

        // Add field declarations
        String fields = message.getFillParameters().toWrite();
        if (fields != null && !fields.isEmpty()) {
            stringBuilder.append(fields);
        }

        if (lombokProperties == null || !lombokProperties.enableLombok()) {
            message.getFillParameters().getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                if (reference != null && vp.getEnumeration() == null) {
                    stringBuilder
                            .append(lineSeparator())
                            .append(generateSetter(reference, uncapitalize(reference)))
                            .append(lineSeparator())
                            .append(generateGetter(reference, uncapitalize(reference)));
                } else if (vp.getEnumeration() == null) {
                    // Regular field (not enum, not reference)
                    stringBuilder
                            .append(lineSeparator())
                            .append(generateSetter(vp.getType(), vp.getName()))
                            .append(lineSeparator())
                            .append(generateGetter(vp.getType(), vp.getName()));
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
