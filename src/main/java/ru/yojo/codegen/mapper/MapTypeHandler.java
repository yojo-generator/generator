package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.ADDITIONAL_PROPERTIES;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

/**
 * Handles map-type properties (additionalProperties in YAML).
 * Supports primitive types, custom objects, nested collections, and custom key types.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class MapTypeHandler implements PropertyTypeHandler {

    private final AbstractMapper abstractMapper;

    public MapTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, ctx.propertiesMap()) != null;
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        abstractMapper.fillMapProperties(
                ctx.variableProperties(),
                ctx.currentSchema(),
                ctx.schemas(),
                ctx.propertiesMap(),
                ctx.processContext()
        );
    }
}
