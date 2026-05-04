package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

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
    public boolean canHandle(String schemaName,
                            VariableProperties variableProperties,
                            Map<String, Object> currentSchema,
                            Map<String, Object> schemas,
                            String propertyName,
                            Map<String, Object> propertiesMap,
                            ProcessContext processContext,
                            Map<String, Object> innerSchemas) {
        return getStringValueIfExistOrElseNull(ADDITIONAL_PROPERTIES, propertiesMap) != null;
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
        abstractMapper.fillMapProperties(variableProperties, currentSchema, schemas, propertiesMap, processContext);
    }
}
