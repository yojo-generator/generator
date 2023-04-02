package ru.yojo.codegen.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Message {

    private String messageName;
    private MessageProperties messageProperties;
    private LombokProperties lombokProperties;

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = getClassBuilder(messageName)
                        .append(messageProperties)
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

        if (!lombokProperties.enableLombok()) {
            messageProperties.getPayload().getVariableProperties().forEach(vp -> {
                String reference = vp.getReference();
                stringBuilder
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
}
