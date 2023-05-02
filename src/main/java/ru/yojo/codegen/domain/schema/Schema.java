package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Schema {

    private String schemaName;
    private String packageName;
    private LombokProperties lombokProperties;
    private FillParameters fillParameters;
    private String extendsFrom;
    private Set<String> implementsFrom = new HashSet<>();
    private Set<String> importSet = new HashSet<>();

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

    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    public Set<String> getImportSet() {
        return importSet;
    }

    public String toWrite() {
        StringBuilder stringBuilder;
        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();
        if (fillParameters.getVariableProperties().stream().anyMatch(variableProperties -> variableProperties.getEnumeration() == null)) {

            stringBuilder =
                    prepareStringBuilder(
                            requiredImports,
                            implementsFrom,
                            extendsFrom,
                            schemaName,
                            importSet,
                            fillParameters
                    );

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

            StringBuilder finalStringBuilder = stringBuilder;
            fillParameters.getVariableProperties().stream()
                    .flatMap(variableProperties -> {
                        Set<String> i = variableProperties.getRequiredImports();
                        if (!lombokProperties.enableLombok()) {
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
            stringBuilder = getEnumClassBuilder(schemaName);
            fillParameters.getVariableProperties().forEach(vp -> vp.getRequiredImports().remove(VALID_IMPORT.getValue()));
            if (fillParameters.getVariableProperties().stream()
                    .anyMatch(variableProperties -> variableProperties.getEnumNames() != null)) {
                VariableProperties value = new VariableProperties();
                value.setType(STRING.getValue());
                value.setName("value");
                if (lombokProperties.enableLombok()) {
                    value.getRequiredImports().add(LOMBOK_GETTER_IMPORT.getValue());
                    value.getAnnotationSet().add(LOMBOK_GETTER_ANNOTATION.getValue());
                }
                fillParameters.getVariableProperties().add(value);
                stringBuilder.append(fillParameters.toWrite())
                        .append(lineSeparator());
            } else {
                stringBuilder.append(fillParameters.toWrite())
                        .append(lineSeparator());
            }

            if (lombokProperties.enableLombok() && lombokProperties.allArgsConstructor() && fillParameters.getVariableProperties().stream()
                    .anyMatch(variableProperties -> variableProperties.getEnumNames() != null)) {
                lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION.getValue())
                        .append(lineSeparator());
                requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT.getValue());
            }
            StringBuilder finalStringBuilder = stringBuilder;
            fillParameters.getVariableProperties().stream()
                    .flatMap(variableProperties -> {
                        Set<String> i = variableProperties.getRequiredImports();
                        if (!lombokProperties.enableLombok()) {
                            finalStringBuilder
                                    .append(lineSeparator())
                                    .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                        }
                        return i.stream();
                    }).forEach(requiredImports::add);
        }
        stringBuilder.insert(0, lombokAnnotationBuilder);
        return finishBuild(stringBuilder, requiredImports, packageName);
    }
}