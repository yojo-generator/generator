package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.lombok.LombokProperties;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Schema {

    /**
     * Name of Schema
     */
    private String schemaName;

    /**
     * Descripton of Schema
     */
    private String description;

    /**
     * Name of package. Used to specify generated class package
     */
    private String packageName;

    /**
     * Properties of Lombok
     */
    private LombokProperties lombokProperties;

    /**
     * General parameters of schema or message
     */
    private FillParameters fillParameters;

    /**
     * Used to specify extending
     */
    private String extendsFrom;

    /**
     * Set of interfaces, which generated class must be implements.
     * Each must be filled with full path like: com.example.path.SomeInterface
     */
    private Set<String> implementsFrom = new HashSet<>();

    /**
     * Set of necessary imports
     */
    private Set<String> importSet = new HashSet<>();

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (fillParameters.getVariableProperties().stream().anyMatch(variableProperties -> variableProperties.getEnumeration() == null) ||
        extendsFrom != null) {

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
                            if (variableProperties.isPrimitive()) {
                                String name = variableProperties.getName();
                                String type = variableProperties.getType();
                                finalStringBuilder
                                        .append(lineSeparator())
                                        .append(format(SETTER, capitalize(name), type, name, name, name))
                                        .append(lineSeparator())
                                        .append(format(GETTER, type, capitalize(name), name));
                            } else {
                                finalStringBuilder
                                        .append(lineSeparator())
                                        .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                        .append(lineSeparator())
                                        .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                            }
                        }
                        if (variableProperties.isPrimitive()) {
                            i.clear();
                        }
                        return i.stream();
                    })
                    .forEach(requiredImports::add);

        } else {
            stringBuilder = getEnumClassBuilder(schemaName);
            fillParameters.getVariableProperties().forEach(vp -> vp.getRequiredImports().remove(VALID_IMPORT));
            //ENUM WITH DESCRIPTION
            if (fillParameters.getVariableProperties().stream()
                    .anyMatch(variableProperties -> variableProperties.getEnumNames() != null)) {
                VariableProperties value = new VariableProperties();
                value.setType(STRING);
                value.setName("value");
                value.setEnum(true);
                if (lombokProperties.enableLombok()) {
                    value.getRequiredImports().add(LOMBOK_GETTER_IMPORT);
                    value.getAnnotationSet().add(LOMBOK_GETTER_ANNOTATION);
                }
                fillParameters.getVariableProperties().add(value);
                stringBuilder.append(fillParameters.toWrite())
                        .append(lineSeparator());
            } else {
                stringBuilder.append(fillParameters.toWrite())
                        .append(lineSeparator());
            }

            if (lombokProperties.enableLombok() &&
                    fillParameters.getVariableProperties().stream()
                    .anyMatch(variableProperties -> variableProperties.getEnumNames() != null)) {
                lombokAnnotationBuilder.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION)
                        .append(lineSeparator());
                requiredImports.add(LOMBOK_ALL_ARGS_CONSTRUCTOR_IMPORT);
            }
            StringBuilder finalStringBuilder = stringBuilder;
            fillParameters.getVariableProperties().stream()
                    .flatMap(variableProperties -> {
                        Set<String> i = variableProperties.getRequiredImports();
                        if (!lombokProperties.enableLombok() && variableProperties.getEnumeration() == null && variableProperties.isEnum()) {
                            finalStringBuilder
                                    .append(lineSeparator())
                                    .append(generateEnumConstructor(schemaName, variableProperties.getType(), variableProperties.getName()));
                        }
                        if (!lombokProperties.enableLombok() && variableProperties.getEnumeration() == null) {
                            finalStringBuilder
                                    .append(lineSeparator())
                                    .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                        }
                        return i.stream();
                    }).forEach(requiredImports::add);
        }
        stringBuilder.insert(0, lombokAnnotationBuilder);

        generateClassJavaDoc(stringBuilder, description);
        return finishBuild(stringBuilder, requiredImports, packageName);
    }
}