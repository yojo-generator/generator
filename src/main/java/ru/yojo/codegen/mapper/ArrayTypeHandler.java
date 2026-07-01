package ru.yojo.codegen.mapper;

import static ru.yojo.codegen.constants.Dictionary.ARRAY;
import static ru.yojo.codegen.constants.Dictionary.ENUMERATION;
import static ru.yojo.codegen.constants.Dictionary.TYPE;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;
import static ru.yojo.codegen.util.MapperUtil.uncapitalize;

/**
 * Handles array-type properties (type: array in YAML).
 * Supports various collection types with custom item types.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ArrayTypeHandler implements PropertyTypeHandler {

    private final AbstractMapper abstractMapper;

    /**
     * Creates a handler for array-type properties.
     *
     * @param abstractMapper parent mapper for delegating type filling
     */
    public ArrayTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(PropertyResolutionContext ctx) {
        String type = getStringValueIfExistOrElseNull(TYPE, ctx.propertiesMap());
        // Don't match if this property has an 'enum' field - it's an enum constant, not an array
        if (getStringValueIfExistOrElseNull(ENUMERATION, ctx.propertiesMap()) != null) {
            return false;
        }
        return ARRAY.equals(uncapitalize(ctx.variableProperties().getType())) ||
               (type != null && ARRAY.equalsIgnoreCase(type));
    }

    @Override
    public void handle(PropertyResolutionContext ctx) {
        abstractMapper.fillArrayProperties(
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
