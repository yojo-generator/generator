package ru.yojo.codegen.domain.schema;

import ru.yojo.codegen.domain.FillParameters;
import ru.yojo.codegen.domain.VariableProperties;
import ru.yojo.codegen.domain.lombok.LombokProperties;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static ru.yojo.codegen.constants.Dictionary.*;
import static ru.yojo.codegen.util.MapperUtil.*;

@SuppressWarnings("all")
public class Schema {

    /**
     * Name of Schema
     */
    private String schemaName;

    /**
     * Descripton of Schema
     */
    private String description;

    /**
     * Name of package. Used to specify generated class package
     */
    private String packageName;

    /**
     * Properties of Lombok
     */
    private LombokProperties lombokProperties;

    /**
     * General parameters of schema or message
     */
    private FillParameters fillParameters;

    /**
     * Used to specify extending
     */
    private String extendsFrom;

    /**
     * Set of interfaces, which generated class must be implements.
     * Each must be filled with full path like: com.example.path.SomeInterface
     */
    private Set<String> implementsFrom = new HashSet<>();

    /**
     * Set of necessary imports
     */
    private Set<String> importSet = new HashSet<>();

    /**
     * Interfaces
     */
    private boolean isInterface;
    private Map<String, Object> methods = new LinkedHashMap<>();
    private Set<String> interfaceImports = new HashSet<>();

    // ——— Getters & Setters ——— //

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLombokProperties(LombokProperties lombokProperties) {
        this.lombokProperties = lombokProperties;
    }

    public void setFillParameters(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    public Set<String> getImplementsFrom() {
        return implementsFrom;
    }

    public Set<String> getImportSet() {
        return importSet;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public Set<String> getInterfaceImports() {
        return interfaceImports;
    }

    public void setInterfaceImports(Set<String> interfaceImports) {
        this.interfaceImports = interfaceImports;
    }

    public Map<String, Object> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, Object> methods) {
        this.methods = methods;
    }

    // ——— CORE: toWrite() ——— //

    public String toWrite() {
        StringBuilder stringBuilder;
        Set<String> requiredImports = new HashSet<>();
        StringBuilder lombokAnnotationBuilder = new StringBuilder();

        if (isInterface) {
            stringBuilder = generateInterface();
        } else {
            boolean hasNonEnumProperties = fillParameters.getVariableProperties().stream()
                    .anyMatch(vp -> vp.getEnumeration() == null);

            if (hasNonEnumProperties) {
                // Regular class
                stringBuilder = prepareStringBuilder(
                        requiredImports,
                        implementsFrom,
                        extendsFrom,
                        schemaName,
                        importSet,
                        fillParameters
                );

                if (lombokProperties.enableLombok()) {
                    if (schemaName.equals("Data") ||
                        fillParameters.getVariableProperties().stream().anyMatch(prop -> "Data".equals(prop.getType()))) {
                        lombokAnnotationBuilder
                                .append(LOMBOK_DATA_ANNOTATION.replace("@", "@lombok."))
                                .append(lineSeparator());
                    } else {
                        lombokAnnotationBuilder
                                .append(LOMBOK_DATA_ANNOTATION)
                                .append(lineSeparator());
                        requiredImports.add(LOMBOK_DATA_IMPORT);
                    }
                    buildLombokAnnotations(lombokProperties, requiredImports, lombokAnnotationBuilder);
                }

                StringBuilder finalStringBuilder = stringBuilder;
                fillParameters.getVariableProperties().stream()
                        .flatMap(variableProperties -> {
                            Set<String> i = variableProperties.getRequiredImports();
                            if (!lombokProperties.enableLombok() && variableProperties.getEnumeration() == null) {
                                if (variableProperties.isPrimitive()) {
                                    String name = variableProperties.getName();
                                    String type = variableProperties.getType();
                                    finalStringBuilder
                                            .append(lineSeparator())
                                            .append(format(SETTER, capitalize(name), type, name, name, name))
                                            .append(lineSeparator())
                                            .append(format(GETTER, type, capitalize(name), name));
                                } else {
                                    finalStringBuilder
                                            .append(lineSeparator())
                                            .append(generateSetter(variableProperties.getType(), variableProperties.getName()))
                                            .append(lineSeparator())
                                            .append(generateGetter(variableProperties.getType(), variableProperties.getName()));
                                }
                            }
                            if (variableProperties.isPrimitive()) {
                                i.clear();
                            }
                            return i.stream();
                        })
                        .forEach(requiredImports::add);
            } else {
                // ENUM
                stringBuilder = getEnumClassBuilder(schemaName);

                // Remove @Valid from enums
                fillParameters.getVariableProperties().forEach(vp -> {
                    vp.getRequiredImports().remove(JAVAX_VALID_IMPORT);
                    vp.getRequiredImports().remove(JAKARTA_VALID_IMPORT);
                });

                boolean hasEnumWithDescription = fillParameters.getVariableProperties().stream()
                        .anyMatch(vp -> vp.getEnumNames() != null);

                if (hasEnumWithDescription) {
                    // 1️⃣ Генерация констант (SUCCESS("..."), ...)
                    StringBuilder constantsBuilder = new StringBuilder();
                    for (int i = 0; i < fillParameters.getVariableProperties().size(); i++) {
                        VariableProperties vp = fillParameters.getVariableProperties().get(i);
                        if (vp.getEnumeration() != null) {
                            String name = vp.getEnumeration();
                            String desc = vp.getEnumNames() != null ? esc(vp.getEnumNames()) : "";
                            constantsBuilder
                                    .append(TABULATION)
                                    .append(name)
                                    .append("(\"")
                                    .append(desc)
                                    .append("\")");
                            if (i < fillParameters.getVariableProperties().size() - 1) {
                                constantsBuilder.append(",");
                            }
                            constantsBuilder.append(lineSeparator());
                        }
                    }

                    // Убираем последний перевод строки и добавляем ';' в той же строке
                    String constants = constantsBuilder.toString();
                    if (constants.endsWith(lineSeparator())) {
                        constants = constants.substring(0, constants.length() - lineSeparator().length());
                    }
                    constants += ";";

                    // 2️⃣ Формируем enum
                    stringBuilder
                            .append(lineSeparator())
                            .append(constants)
                            .append(lineSeparator())
                            .append(lineSeparator())
                            .append("    private final String value;")
                            .append(lineSeparator())
                            .append(lineSeparator())
                            .append("    ")
                            .append(schemaName)
                            .append("(String value) {")
                            .append(lineSeparator())
                            .append("        this.value = value;")
                            .append(lineSeparator())
                            .append("    }")
                            .append(lineSeparator());

                    // 3️⃣ @Getter на уровне класса
                    if (lombokProperties.enableLombok()) {
                        lombokAnnotationBuilder.append("@Getter").append(lineSeparator());
                        requiredImports.add(LOMBOK_GETTER_IMPORT);
                    } else {
                        stringBuilder
                                .append(lineSeparator())
                                .append("    public String getValue() {")
                                .append(lineSeparator())
                                .append("        return value;")
                                .append(lineSeparator())
                                .append("    }")
                                .append(lineSeparator());
                    }

                } else {
                    // обычный enum (без описания)
                    stringBuilder.append(fillParameters.toWrite()).append(lineSeparator());
                }

                // 4️⃣ Lombok (без @NoArgsConstructor для enum с конструктором)
                if (lombokProperties.enableLombok()) {
                    LombokProperties effectiveLombok = LombokProperties.newLombokProperties(lombokProperties);
                    if (hasEnumWithDescription) {
                        effectiveLombok.setNoArgsConstructor(false);
                    }
                    buildLombokAnnotations(effectiveLombok, requiredImports, lombokAnnotationBuilder);
                }
            }
        }

        stringBuilder.insert(0, lombokAnnotationBuilder);

        if (!isInterface) {
            generateClassJavaDoc(stringBuilder, description);
        }

        return finishBuild(stringBuilder, requiredImports, packageName);
    }

    // ——— Вспомогательные методы ——— //

    private StringBuilder generateInterface() {
        StringBuilder stringBuilder = getInterfaceBuilder(schemaName);
        generateClassJavaDoc(stringBuilder, description);
        if (!methods.isEmpty()) {
            methods.values().forEach(method -> {
                Map<String, Object> currentMethod = castObjectToMap(method);
                String methodDescription = getStringValueIfExistOrElseNull(DESCRIPTION, currentMethod);
                String methodDefinition = getStringValueIfExistOrElseNull(DEFINITION, currentMethod);
                if (methodDescription != null) {
                    generateJavaDoc(stringBuilder, methodDescription, null);
                }
                methodDefinition = TABULATION.concat(methodDefinition.endsWith(";") ? methodDefinition : methodDefinition + ";");
                stringBuilder
                        .append(lineSeparator())
                        .append(methodDefinition)
                        .append(lineSeparator());
            });
        }
        if (!interfaceImports.isEmpty()) {
            stringBuilder.insert(0, lineSeparator());
            interfaceImports.forEach(i -> {
                i = IMPORT.concat(i.endsWith(";") ? i : i + ";");
                stringBuilder.insert(0, i + lineSeparator());
            });
        }
        return stringBuilder;
    }

    //  Экранирование кавычек
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}