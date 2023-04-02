package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;

public class SchemaProperties {

    private List<SchemaVariableProperties> schemaVariableProperties;

    public List<SchemaVariableProperties> getVariableProperties() {
        return schemaVariableProperties;
    }

    public void setVariableProperties(List<SchemaVariableProperties> schemaVariableProperties) {
        this.schemaVariableProperties = schemaVariableProperties;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        schemaVariableProperties.forEach(vp ->
                stringBuilder
                        .append(lineSeparator())
                        .append(vp));

        return stringBuilder.toString();
    }

}
