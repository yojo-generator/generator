package ru.yojo.codegen.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;

@SuppressWarnings("all")
public class FillParameters {

    public FillParameters() {
    }

    private List<VariableProperties> variableProperties;

    private Set<String> validationGroups = new HashSet<>();
    private Set<String> validationGroupsImports = new HashSet<>();

    public Set<String> getValidationGroups() {
        return validationGroups;
    }

    public void setValidationGroups(Set<String> validationGroups) {
        this.validationGroups = validationGroups;
    }

    public Set<String> getValidationGroupsImports() {
        return validationGroupsImports;
    }

    public void setValidationGroupsImports(Set<String> validationGroupsImports) {
        this.validationGroupsImports = validationGroupsImports;
    }

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

                    Comparator<String> stringComparator = (a, b) -> Integer.compare(a.length(), b.length());
                    variableProperties.get(i).getAnnotationSet().stream().sorted().sorted(stringComparator).forEach(annotation -> {
                        stringBuilder.append(lineSeparator())
                                .append(TABULATION)
                                .append(annotation);
                    });
                    stringBuilder.append(lineSeparator())
                            .append(String.format(FIELD, variableProperties.get(i).getType(), variableProperties.get(i).getName())).toString();
                } else if (variableProperties.stream().anyMatch(vp -> vp.getEnumNames() != null)) {
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION)
                            .append(variableProperties.get(i).getType())
                            .append(i == variableProperties.size() - 2 ? ";" : ",")
                            .append(i == variableProperties.size() - 2 ? lineSeparator() : "")
                            .toString();
                } else {
                    stringBuilder.append(lineSeparator())
                            .append(TABULATION)
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
