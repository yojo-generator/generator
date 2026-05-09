package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.ARRAY;
import static ru.yojo.codegen.constants.Dictionary.REFERENCE;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;
import static ru.yojo.codegen.util.MapperUtil.uncapitalize;

/**
 * Handles reference-type properties ($ref in YAML).
 * Resolves references to other schemas and sets up the appropriate type.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ReferenceTypeHandler implements PropertyTypeHandler {

    private final AbstractMapper abstractMapper;

    public ReferenceTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        return getStringValueIfExistOrElseNull(REFERENCE, ctx.propertiesMap()) != null &&
               !ARRAY.equals(uncapitalize(ctx.variableProperties().getType()));
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        abstractMapper.fillReferenceProperties(
                ctx.schemaName(),
                ctx.variableProperties(),
                ctx.currentSchema(),
                ctx.schemas(),
                ctx.propertyName(),
                ctx.propertiesMap(),
                ctx.processContext(),
                ctx.innerSchemas()
        );
    }
}
