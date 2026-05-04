package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

import static ru.yojo.codegen.constants.Dictionary.ARRAY;
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

    public ArrayTypeHandler(AbstractMapper abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Override
    public boolean canHandle(String schemaName,
                            VariableProperties variableProperties,
                            Map<String, Object> currentSchema,
                            Map<String, Object> schemas,
                            String propertyName,
                            Map<String, Object> propertiesMap,
                            ProcessContext processContext,
                            Map<String, Object> innerSchemas) {
        String type = getStringValueIfExistOrElseNull(TYPE, propertiesMap);
        return ARRAY.equals(uncapitalize(variableProperties.getType())) ||
               (type != null && ARRAY.equalsIgnoreCase(type));
    }

    @Override
    public void handle(String schemaName,
                       VariableProperties variableProperties,
                       Map<String, Object> currentSchema,
                       Map<String, Object> schemas,
                       String propertyName,
                       Map<String, Object> propertiesMap,
                       ProcessContext processContext,
                       Map<String, Object> innerSchemas) {
        abstractMapper.fillArrayProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
    }
}
