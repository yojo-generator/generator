package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;

@SuppressWarnings("all")
public class FillParameters {

    public FillParameters() {
    }

    private List<VariableProperties> variableProperties;

    public FillParameters(List<VariableProperties> variableProperties) {
        this.variableProperties = variableProperties;
    }

    public List<VariableProperties> getVariableProperties() {
        return variableProperties;
    }

    public void setVariableProperties(List<VariableProperties> schemaVariableProperties) {
        this.variableProperties = schemaVariableProperties;
    }

    public String toWrite() {
        StringBuilder stringBuilder = new StringBuilder();
        variableProperties.forEach(vp ->
                stringBuilder
                        .append(lineSeparator())
                        .append(vp.toWrite()));
        return stringBuilder.toString();
    }
}
