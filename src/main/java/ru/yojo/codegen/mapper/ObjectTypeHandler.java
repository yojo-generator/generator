package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

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
    public boolean canHandle(String schemaName,
                            VariableProperties variableProperties,
                            Map<String, Object> currentSchema,
                            Map<String, Object> schemas,
                            String propertyName,
                            Map<String, Object> propertiesMap,
                            ProcessContext processContext,
                            Map<String, Object> innerSchemas) {
        return OBJECT_TYPE.equals(variableProperties.getType()) &&
               getStringValueIfExistOrElseNull(PROPERTIES, propertiesMap) != null;
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
        abstractMapper.fillObjectProperties(schemaName, variableProperties, schemas, propertyName, propertiesMap, processContext, innerSchemas);
    }
}
