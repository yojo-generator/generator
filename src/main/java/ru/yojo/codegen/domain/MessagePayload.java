package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.JAVA_DEFAULT_TYPES;

@SuppressWarnings("all")
public class MessagePayload {

    private List<MessageVariableProperties> variableProperties;

    public MessagePayload(List<MessageVariableProperties> variableProperties) {
        this.variableProperties = variableProperties;
    }

    public void setVariableProperties(List<MessageVariableProperties> variableProperties) {
        this.variableProperties = variableProperties;
    }

    public List<MessageVariableProperties> getVariableProperties() {
        return variableProperties;
    }

    public boolean isNeededCommonImport() {
        return variableProperties.stream()
                .anyMatch(variableProperties ->
                        variableProperties.getType() != null &&
                                !JAVA_DEFAULT_TYPES.contains(variableProperties.getType()) &&
                                variableProperties.getReference() != null);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        variableProperties.forEach(variableProperties ->
                stringBuilder.append(lineSeparator())
                        .append(variableProperties.toString()));
        return stringBuilder.toString();
    }
}
