package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

import static ru.yojo.codegen.constants.Dictionary.OBJECT_TYPE;

/**
 * Handles existing object references (format: existing in YAML).
 * Sets up the type to reference an already existing Java class.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class ExistingObjectHandler implements PropertyTypeHandler {

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
               variableProperties.getPackageOfExisingObject() != null &&
               variableProperties.getNameOfExisingObject() != null;
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
        AbstractMapper.fillExistingObjectProperties(variableProperties);
    }
}
