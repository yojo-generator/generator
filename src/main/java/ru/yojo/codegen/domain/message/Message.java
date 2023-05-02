package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.LombokProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
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

    public LombokProperties getLombokProperties() {
        return lombokProperties;
    }

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

    public String getCommonPackageName() {
        return commonPackageName;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public FillParameters getFillParameters() {
        return fillParameters;
    }

    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    public void setImplementsFrom(Set<String> implementsFrom) {
        this.implementsFrom = implementsFrom;
    }

    public Set<String> getImportSet() {
        return importSet;
    }

    public void setImportSet(Set<String> importSet) {
        this.importSet = importSet;
    }

    public String toWrite() {

        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();
        StringBuilder stringBuilder = null;

        if (!implementsFrom.isEmpty() && isNotBlank(extendsFrom)) {
            stringBuilder = getExtendsWithImplementationClassBuilder(messageName, extendsFrom, implementsFrom);
        } else if (!implementsFrom.isEmpty() && isBlank(extendsFrom)) {
            stringBuilder = getImplementationClassBuilder(messageName, implementsFrom);
        } else if (implementsFrom.isEmpty() && isNotBlank(extendsFrom)) {
            stringBuilder = getExtendsClassBuilder(messageName, extendsFrom);
        } else {
            stringBuilder = getClassBuilder(messageName);
        }

        requiredImports.addAll(importSet);
        stringBuilder.append(fillParameters.toWrite())
                .append(lineSeparator());

        if (lombokProperties.enableLombok()) {
            if (fillParameters.getVariableProperties().stream()
                    .anyMatch(prop -> "Data".equals(prop.getType()))) {
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
        if (!lombokProperties.enableLombok()) {
            StringBuilder finalStringBuilder = stringBuilder;
            fillParameters.getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                finalStringBuilder
                        .append(lineSeparator())
                        .append(generateSetter(reference, uncapitalize(reference)))
                        .append(lineSeparator())
                        .append(generateGetter(reference, uncapitalize(reference)));
            });
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        if (isNotBlank(summary)) {
            StringBuilder javadoc = new StringBuilder();
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_START.getValue());
            javadoc.append(lineSeparator()).append(formatString(JAVA_DOC_CLASS_LINE, summary));
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_END.getValue());
            javadoc.append(lineSeparator());

            stringBuilder.insert(0, javadoc);
        }

        fillParameters.getVariableProperties().forEach(vp -> requiredImports.addAll(vp.getRequiredImports()));

        return finishBuild(stringBuilder, requiredImports, messagePackageName);
    }
}
