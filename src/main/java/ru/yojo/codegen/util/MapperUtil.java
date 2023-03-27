package ru.yojo.codegen.util;

import ru.yojo.codegen.constants.ConstantsEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.yojo.codegen.constants.ConstantsEnum.*;

@SuppressWarnings("all")
public class MapperUtil {

    public static Set<String> getSetValueIfExistsOrElseEmptySet(String value, Map<String, Object> schemaMap) {
        Set<String> values = new HashSet<>();
        if (schemaMap.containsKey(value)) {
            values.addAll((ArrayList<String>) schemaMap.get(value));
        }
        return values;
    }

    public static String getStringValueIfExistOrElseNull(ConstantsEnum valueEnum, Map<String, Object> map) {
        if (map.containsKey(valueEnum.getValue())) {
            return map.get(valueEnum.getValue()).toString();
        }
        return null;
    }

    public static Map<String, Object> castObjectToMap(Object map) {
        return (Map<String, Object>) map;
    }

    public static String refReplace(String ref) {
        return capitalize(ref.replaceAll(".+/", ""));
    }

    public static String generateSetter(String type, String variableName) {
        return String.format(SETTER.getValue(), capitalize(variableName), capitalize(type), variableName, variableName, variableName);
    }

    public static String generateGetter(String type, String variableName) {
        return String.format(GETTER.getValue(), capitalize(type), capitalize(variableName), variableName);
    }

    public static void generateValidAnnotation(StringBuilder stringBuilder) {
        stringBuilder.append(lineSeparator())
                .append(VALID_ANNOTATION.getValue());
    }

    public static StringBuilder getClassBuilder(String schemaName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("public class ")
                .append(schemaName)
                .append(" {");
        return stringBuilder;
    }

    /**
     * The method adds to a JavaDoc file
     *
     * @param stringBuilder StringBuilder
     * @param description   Description
     * @param enumeration   Enum
     * @param example       Example
     */
    public static void generateJavaDoc(StringBuilder stringBuilder, String description, String enumeration, String example) {
        if (isNoneEmpty(description) || isNoneEmpty(enumeration) || isNoneEmpty(example)) {
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_START.getValue());
            if (isNotBlank(description)) {
                stringBuilder.append(lineSeparator()).append(formatString(JAVA_DOC_LINE, description));
            }
            if (isNotBlank(example)) {
                stringBuilder.append(lineSeparator()).append(formatString(JAVA_DOC_EXAMPLE, example));
            }
            if (isNotBlank(enumeration)) {
                stringBuilder.append(lineSeparator()).append(formatString(JAVA_DOC_LINE, enumeration));
            }
            stringBuilder.append(lineSeparator()).append(JAVA_DOC_END.getValue());
        }
    }
}
