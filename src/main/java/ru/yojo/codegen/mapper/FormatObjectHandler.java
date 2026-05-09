package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER;
import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Handles properties with {@code type: object} and a recognized {@code format} value
 * (e.g., {@code type: object, format: uuid} → sets format to {@code uuid}).
 * <p>
 * This is a special case where the "object" type is used as a container for
 * a well-known format that maps to a specific Java type (e.g., UUID, LocalDate).
 */
public class FormatObjectHandler implements PropertyTypeHandler {

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        String type = getStringValueIfExistOrElseNull("type", ctx.propertiesMap());
        String format = getStringValueIfExistOrElseNull("format", ctx.propertiesMap());
        return OBJECT_TYPE.equalsIgnoreCase(type)
                && format != null
                && JAVA_LOWER_CASE_TYPES_CHECK_CONVERTER.containsKey(format);
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        String format = getStringValueIfExistOrElseNull("format", ctx.propertiesMap());
        ctx.variableProperties().setFormat(format);
    }
}
