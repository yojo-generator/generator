package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;

/**
 * Handles existing object references (format: existing in YAML).
 * Sets up the type to reference an already existing Java class.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ExistingObjectHandler implements PropertyTypeHandler {

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return OBJECT_TYPE.equals(ctx.variableProperties().getType()) &&
               ctx.variableProperties().getPackageOfExisingObject() != null &&
               ctx.variableProperties().getNameOfExisingObject() != null;
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        AbstractMapper.fillExistingObjectProperties(ctx.variableProperties());
    }
}
