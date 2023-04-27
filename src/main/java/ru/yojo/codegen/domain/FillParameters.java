package ru.yojo.codegen.domain;

import java.util.List;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.ConstantsEnum.*;

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
        if (variableProperties.stream().anyMatch(vp -> vp.getEnumeration() != null)) {
            stringBuilder.append(lineSeparator());
            for (int i = 0; i < variableProperties.size(); i++) {
                if (JAVA_DEFAULT_TYPES.contains(variableProperties.get(i).getType())) {
                    variableProperties.get(i).getAnnotationSet().forEach(annotation -> {
                        stringBuilder.append(lineSeparator())
                                .append(TABULATION.getValue())
                                .append(annotation);
                    });
                    stringBuilder.append(lineSeparator())
                            .append(formatString(FIELD, variableProperties.get(i).getType(), variableProperties.get(i).getName())).toString();
                } else if (variableProperties.stream().anyMatch(vp -> vp.getEnumNames() != null)) {
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION.getValue())
                            .append(variableProperties.get(i).getType())
                            .append(i == variableProperties.size() - 2 ? ";" : ",")
                            .append(i == variableProperties.size() - 2 ? lineSeparator() : "")
                            .toString();
                } else {
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION.getValue())
                            .append(variableProperties.get(i).getType())
                            .append(i == variableProperties.size() - 1 ? ";" : ",")
                            .toString();
                }
            }
            return stringBuilder.toString();
        } else {
            variableProperties.forEach(vp ->
                    stringBuilder
                            .append(lineSeparator())
                            .append(vp.toWrite()));
            return stringBuilder.toString();
        }
    }
}
