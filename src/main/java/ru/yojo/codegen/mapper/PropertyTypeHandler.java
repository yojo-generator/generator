package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

/**
 * Strategy interface for handling different types of YAML property definitions.
 * Each handler is responsible for detecting if it can handle a given property
 * and then populating the appropriate fields in VariableProperties.
 *
 * <p>This follows the Chain of Responsibility pattern where handlers are
 * evaluated in order until one accepts the property.</p>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public interface PropertyTypeHandler {

    /**
     * Checks if this handler can process the given property definition.
     *
     * @param schemaName         name of the containing schema
     * @param variableProperties current field being populated
     * @param currentSchema      full schema map where this property is defined
     * @param schemas            global map of all known schemas
     * @param propertyName       field name in YAML
     * @param propertiesMap      raw field definition from YAML
     * @param processContext     generation context
     * @param innerSchemas       accumulator for inner schemas
     * @return true if this handler can handle the property
     */
    boolean canHandle(String schemaName,
                      VariableProperties variableProperties,
                      Map<String, Object> currentSchema,
                      Map<String, Object> schemas,
                      String propertyName,
                      Map<String, Object> propertiesMap,
                      ProcessContext processContext,
                      Map<String, Object> innerSchemas);

    /**
     * Handles the property by populating VariableProperties with the appropriate
     * type, imports, and other metadata.
     *
     * @param schemaName         name of the containing schema
     * @param variableProperties target field container to populate
     * @param currentSchema      full schema map where this property is defined
     * @param schemas            global map of all known schemas
     * @param propertyName       field name in YAML
     * @param propertiesMap      raw field definition from YAML
     * @param processContext     generation context
     * @param innerSchemas       accumulator for inner schemas
     */
    void handle(String schemaName,
                VariableProperties variableProperties,
                Map<String, Object> currentSchema,
                Map<String, Object> schemas,
                String propertyName,
                Map<String, Object> propertiesMap,
                ProcessContext processContext,
                Map<String, Object> innerSchemas);
}
