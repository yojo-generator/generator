package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.Map;

/**
 * Context object that bundles parameters passed to {@link PropertyTypeHandler} methods.
 * <p>
 * Reduces method parameter count and simplifies the handler interface.
 *
 * @param schemaName         name of the containing schema
 * @param variableProperties target field container to populate
 * @param currentSchema      full schema map where this property is defined
 * @param schemas            global map of all known schemas
 * @param propertyName       field name in YAML
 * @param propertiesMap      raw field definition from YAML
 * @param processContext     current generation context
 * @param innerSchemas       accumulator for discovered inner schemas
 */
public record PropertyResolutionContext(
        String schemaName,
        VariableProperties variableProperties,
        Map<String, Object> currentSchema,
        Map<String, Object> schemas,
        String propertyName,
        Map<String, Object> propertiesMap,
        ProcessContext processContext,
        Map<String, Object> innerSchemas
) {
}
