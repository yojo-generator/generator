package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

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
    public boolean canHandle(String schemaName,
                            VariableProperties variableProperties,
                            Map<String, Object> currentSchema,
                            Map<String, Object> schemas,
                            String propertyName,
                            Map<String, Object> propertiesMap,
                            ProcessContext processContext,
                            Map<String, Object> innerSchemas) {
        return (OBJECT_TYPE.equals(variableProperties.getType()) || STRING.equals(variableProperties.getType())) &&
               getStringValueIfExistOrElseNull(ENUMERATION, propertiesMap) != null;
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
        abstractMapper.fillEnumProperties(schemaName, variableProperties, propertyName, propertiesMap, processContext, innerSchemas);
    }
}
