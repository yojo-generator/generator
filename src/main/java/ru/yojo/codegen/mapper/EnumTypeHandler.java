package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.ENUMERATION;
import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;
import static ru.yojo.codegen.constants.Dictionary.STRING;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Handles enum-type properties (enum field in YAML).
 * Supports both object and string types with enum values.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class EnumTypeHandler implements PropertyTypeHandler {

    private final AbstractMapper abstractMapper;

    public EnumTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return (OBJECT_TYPE.equals(ctx.variableProperties().getType()) || STRING.equals(ctx.variableProperties().getType())) &&
               getStringValueIfExistOrElseNull(ENUMERATION, ctx.propertiesMap()) != null;
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        abstractMapper.fillEnumProperties(
                ctx.schemaName(),
                ctx.variableProperties(),
                ctx.propertyName(),
                ctx.propertiesMap(),
                ctx.processContext(),
                ctx.innerSchemas()
        );
    }
}
