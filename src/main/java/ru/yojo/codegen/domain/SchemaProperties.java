package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;

public class SchemaProperties {

    private List<VariableProperties> variableProperties;

    public List<VariableProperties> getVariableProperties() {
        return variableProperties;
    }

    public void setVariableProperties(List<VariableProperties> variableProperties) {
        this.variableProperties = variableProperties;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        variableProperties.forEach(vp ->
                stringBuilder
                        .append(lineSeparator())
                        .append(vp));

        return stringBuilder.toString();
    }

}
