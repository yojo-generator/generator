package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;
import static ru.yojo.codegen.constants.Dictionary.PROPERTIES;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Handles object-type properties with nested properties (type: object with properties field).
 * Creates inner schemas for nested objects.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ObjectTypeHandler implements PropertyTypeHandler {

    private final AbstractMapper abstractMapper;

    public ObjectTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return OBJECT_TYPE.equals(ctx.variableProperties().getType()) &&
               getStringValueIfExistOrElseNull(PROPERTIES, ctx.propertiesMap()) != null;
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        abstractMapper.fillObjectProperties(
                ctx.schemaName(),
                ctx.variableProperties(),
                ctx.schemas(),
                ctx.propertyName(),
                ctx.propertiesMap(),
                ctx.processContext(),
                ctx.innerSchemas()
        );
    }
}
