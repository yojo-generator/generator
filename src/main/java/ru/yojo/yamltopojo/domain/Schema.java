package ru.yojo.yamltopojo.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.yamltopojo.constants.ConstantsEnum.*;
import static ru.yojo.yamltopojo.util.MapperUtil.generateGetter;
import static ru.yojo.yamltopojo.util.MapperUtil.generateSetter;

public class Schema {

    private String schemaName;

    private LombokProperties lombokProperties;

    private SchemaProperties schemaProperties;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public void setSchemaProperties(SchemaProperties schemaProperties) {
        this.schemaProperties = schemaProperties;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = getClassBuilder(schemaName)
                .append(schemaProperties)
                .append(lineSeparator());

        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();

        if (lombokProperties.enableLombok()) {
            lombokAnnotationBuilder
                    .append(LOMBOK_DATA_ANNOTATION.getValue())
                    .append(lineSeparator())
                    .append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION.getValue())
                    .append(lineSeparator());
            requiredImports.addAll(List.of(LOMBOK_DATA_IMPORT.getValue(), LOMBOK_NO_ARGS_CONSTRUCTOR_IMPORT.getValue()));
            if (lombokProperties.accessors()) {
                lombokAnnotationBuilder.append(LOMBOK_ACCESSORS_ANNOTATION.getValue())
                        .append(lineSeparator());
                requiredImports.add(LOMBOK_ACCESSORS_IMPORT.getValue());
            }
            if (lombokProperties.allArgsConstructor()) {
                lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION.getValue())
                        .append(lineSeparator());
                requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT.getValue());
            }
        }

        schemaProperties.getVariableProperties().stream()
                .flatMap(variableProperties -> {
                    Set<String> i = variableProperties.getRequiredImports();
                    if (!lombokProperties.enableLombok()) {
                        stringBuilder
                                .append(lineSeparator())
                                .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                .append(lineSeparator())
                                .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                    }
                    return i.stream();
                }).forEach(requiredImports::add);

        stringBuilder.insert(0, lombokAnnotationBuilder);

        StringBuilder importBuilder = new StringBuilder();
        requiredImports.forEach(requiredImport -> importBuilder
                .append(IMPORT.getValue())
                .append(requiredImport)
                .append(lineSeparator()));
        stringBuilder.insert(0, importBuilder.append(lineSeparator()));
        
        return stringBuilder
                .append(lineSeparator())
                .append("}")
                .toString();
    }

    private static StringBuilder getClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }
}