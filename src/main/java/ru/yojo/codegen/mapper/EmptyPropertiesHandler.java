package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;

/**
 * Handles the edge case where the properties map is empty.
 * <p>
 * This occurs when a YAML property definition has no {@code type}, {@code $ref},
 * or any other recognizable field — typically representing an untyped placeholder.
 * Sets the type to "Object" and marks it as not valid for further processing.
 */
public class EmptyPropertiesHandler implements PropertyTypeHandler {

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return ctx.propertiesMap().isEmpty();
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        ctx.variableProperties().setType(OBJECT_TYPE);
        ctx.variableProperties().setValid(false);
    }
}
