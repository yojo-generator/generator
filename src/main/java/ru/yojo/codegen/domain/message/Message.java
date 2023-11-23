package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Message {

    private String summary;
    private FillParameters fillParameters;
    private String messageName;

    private String messagePackageName;
    private String commonPackageName;

    private LombokProperties lombokProperties;
    private String extendsFrom;
    private Set<String> implementsFrom = new HashSet<>();
    private Set<String> importSet = new HashSet<>();

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public void setMessagePackageName(String messagePackageName) {
        this.messagePackageName = messagePackageName;
    }

    public void setCommonPackageName(String commonPackageName) {
        this.commonPackageName = commonPackageName;
    }

    public String getExtendsFrom() {
        return extendsFrom;
    }
    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void enrichFillParameters(FillParameters fillParameters) {
        this.fillParameters.getVariableProperties().addAll(fillParameters.getVariableProperties());
    }

    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    public Set<String> getImportSet() {
        return importSet;
    }

    public String toWrite() {

        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();
        StringBuilder stringBuilder = prepareStringBuilder(
                requiredImports,
                implementsFrom,
                extendsFrom,
                messageName,
                importSet,
                fillParameters
        );

        if (lombokProperties.enableLombok()) {
            if (fillParameters.getVariableProperties().stream()
                    .anyMatch(prop -> "Data".equals(prop.getType()))) {
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
        if (!lombokProperties.enableLombok()) {
            fillParameters.getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                if (reference != null && vp.getEnumeration() == null) {
                    stringBuilder
                            .append(lineSeparator())
                            .append(generateSetter(reference, uncapitalize(reference)))
                            .append(lineSeparator())
                            .append(generateGetter(reference, uncapitalize(reference)));
                } else {
                    fillParameters.getVariableProperties().stream()
                            .filter(varProp -> vp.equals(varProp))
                            .flatMap(variableProperties -> {
                                Set<String> i = variableProperties.getRequiredImports();
                                if (!variableProperties.isEnum()) {
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

        stringBuilder.insert(0, lombokAnnotationBuilder);

        if (isNotBlank(summary)) {
            StringBuilder javadoc = new StringBuilder();
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_START);
            javadoc.append(lineSeparator()).append(String.format(JAVA_DOC_CLASS_LINE, summary));
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_END);
            javadoc.append(lineSeparator());

            stringBuilder.insert(0, javadoc);
        }

        fillParameters.getVariableProperties().forEach(vp -> requiredImports.addAll(vp.getRequiredImports()));

        return finishBuild(stringBuilder, requiredImports, messagePackageName);
    }
}
