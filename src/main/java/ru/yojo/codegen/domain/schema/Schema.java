package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.LOMBOK_DATA_ANNOTATION;
import static ru.yojo.codegen.constants.ConstantsEnum.LOMBOK_DATA_IMPORT;
import static ru.yojo.codegen.util.MapperUtil.*;

public class Schema {

    private String schemaName;

    private String packageName;

    private LombokProperties lombokProperties;

    private FillParameters fillParameters;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String toWrite() {
        StringBuilder stringBuilder = getClassBuilder(schemaName)
                .append(fillParameters.toWrite())
                .append(lineSeparator());

        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();

        if (lombokProperties.enableLombok()) {
            if (schemaName.equals("Data") || fillParameters.getVariableProperties().stream().anyMatch(prop -> "Data".equals(prop.getType()))) {
                lombokAnnotationBuilder
                        .append(LOMBOK_DATA_ANNOTATION.getValue().replace("@", "@lombok."))
                        .append(lineSeparator());
            } else {
                lombokAnnotationBuilder
                        .append(LOMBOK_DATA_ANNOTATION.getValue())
                        .append(lineSeparator());
                requiredImports.add(LOMBOK_DATA_IMPORT.getValue());
            }
            buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
        }

        fillParameters.getVariableProperties().stream()
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

        return finishBuild(stringBuilder, requiredImports, packageName);
    }
}