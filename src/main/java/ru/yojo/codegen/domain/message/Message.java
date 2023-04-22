package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.LombokProperties;
import ru.yojo.codegen.domain.MessageImplementationProperties;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Message {

    private String messageName;
    private String messagePackageName;
    private String commonPackageName;
    private MessageProperties messageProperties;
    private LombokProperties lombokProperties;
    private String extendsFrom;
    private MessageImplementationProperties messageImplementationProperties;

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }

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

    public MessageImplementationProperties getMessageImplementationProperties() {
        return messageImplementationProperties;
    }

    public void setMessageImplementationProperties(MessageImplementationProperties messageImplementationProperties) {
        this.messageImplementationProperties = messageImplementationProperties;
    }

    public String getExtendsFrom() {
        return extendsFrom;
    }

    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    public String toWrite() {

        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();
        StringBuilder stringBuilder = null;

        if (messageImplementationProperties != null && isNotBlank(messageImplementationProperties.getImplementationClass())) {
            if (extendsFrom != null) {
                stringBuilder = getExtendsWithImplementationClassBuilder(messageName, extendsFrom, messageImplementationProperties.getImplementationClass());
                requiredImports.add(commonPackageName.replace(";", "") + "." + extendsFrom.replace("extends ", "") + ";");
            } else {
                stringBuilder = getImplementationClassBuilder(messageName, messageImplementationProperties.getImplementationClass());
            }
            if (messageImplementationProperties.getImplementationPackage() != null) {
                requiredImports.add(messageImplementationProperties.getImplementationPackage() + "." + messageImplementationProperties.getImplementationClass() + ";");
            }
        } else {
            if (extendsFrom != null && messageImplementationProperties == null && isBlank(messageImplementationProperties.getImplementationClass())) {
                stringBuilder = getExtendsClassBuilder(messageName, extendsFrom);
                requiredImports.add(commonPackageName + "." + extendsFrom.replace("extends ", "") + ";");
            }
            stringBuilder = getClassBuilder(messageName);
        }
        stringBuilder.append(messageProperties.toWrite())
                .append(lineSeparator());

        if (lombokProperties.enableLombok()) {
            if (messageProperties.getPayload().getVariableProperties().stream()
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
            messageProperties.getPayload().getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                finalStringBuilder
                        .append(lineSeparator())
                        .append(generateSetter(reference, uncapitalize(reference)))
                        .append(lineSeparator())
                        .append(generateGetter(reference, uncapitalize(reference)));
            });
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        if (isNotBlank(messageProperties.getSummary())) {
            StringBuilder javadoc = new StringBuilder();
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_START.getValue());
            javadoc.append(lineSeparator()).append(formatString(JAVA_DOC_CLASS_LINE, messageProperties.getSummary()));
            javadoc.append(lineSeparator()).append(JAVA_DOC_CLASS_END.getValue());
            javadoc.append(lineSeparator());

            stringBuilder.insert(0, javadoc);
        }

        messageProperties.getPayload().getVariableProperties().forEach(vp -> requiredImports.addAll(vp.getRequiredImports()));

        return finishBuild(stringBuilder, requiredImports, messagePackageName);
    }
}
