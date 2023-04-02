package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        variableProperties.forEach(variableProperties ->
        stringBuilder.append(lineSeparator())
                .append(variableProperties.toString()));
        return stringBuilder.toString();
    }
}
