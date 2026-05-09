package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;
import static ru.yojo.codegen.util.MapperUtil.capitalize;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Catch-all handler that processes any property not matched by more specific handlers.
 * <p>
 * This is the LAST handler in the chain and always returns {@code true}.
 * It handles simple types (e.g., {@code type: string}, {@code type: integer}),
 * as well as plain objects without nested properties, enums, references, etc.
 */
public class DefaultTypeHandler implements PropertyTypeHandler {

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return true; // Catch-all — always handles remaining cases
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        var variableProperties = ctx.variableProperties();
        var propertiesMap = ctx.propertiesMap();

        // Normalize type from raw YAML — default to "Object" if not specified
        String type = getStringValueIfExistOrElseNull("type", propertiesMap);
        variableProperties.setType(capitalize(type != null ? type : OBJECT_TYPE));

        // If the final type is "Object" and no specific handler matched,
        // mark as not valid (will be treated as a generic object reference)
        if (OBJECT_TYPE.equals(variableProperties.getType())) {
            variableProperties.setType(OBJECT_TYPE);
            variableProperties.setValid(false);
        }

        // Always pass through the format value (may be null)
        String format = getStringValueIfExistOrElseNull("format", propertiesMap);
        variableProperties.setFormat(format);
    }
}
