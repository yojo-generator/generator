package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

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
    public boolean canHandle(String schemaName,
                            VariableProperties variableProperties,
                            Map<String, Object> currentSchema,
                            Map<String, Object> schemas,
                            String propertyName,
                            Map<String, Object> propertiesMap,
                            ProcessContext processContext,
                            Map<String, Object> innerSchemas) {
        return getStringValueIfExistOrElseNull(REFERENCE, propertiesMap) != null &&
               !ARRAY.equals(uncapitalize(variableProperties.getType()));
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
        abstractMapper.fillReferenceProperties(schemaName, variableProperties, currentSchema, schemas, propertyName, propertiesMap, processContext, innerSchemas);
    }
}
