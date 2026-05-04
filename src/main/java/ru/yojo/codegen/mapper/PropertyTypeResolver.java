package ru.yojo.codegen.mapper;

import ru.yojo.codegen.context.ProcessContext;
import ru.yojo.codegen.domain.VariableProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resolver that uses Chain of Responsibility pattern to find the appropriate
 * handler for a given property type.
 *
 * <p>Handlers are evaluated in order, and the first handler that can handle
 * the property is used to process it.</p>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class PropertyTypeResolver {

    private final List<PropertyTypeHandler> handlers = new ArrayList<>();

    /**
     * Creates a resolver with the default handler chain.
     * The order of handlers is important - more specific handlers should come first.
     *
     * @param abstractMapper the mapper instance used by handlers
     */
    public PropertyTypeResolver(AbstractMapper abstractMapper) {
        // Order matters! More specific checks should come first
        handlers.add(new MapTypeHandler(abstractMapper));
        handlers.add(new ArrayTypeHandler(abstractMapper));
        handlers.add(new ReferenceTypeHandler(abstractMapper));
        handlers.add(new ObjectTypeHandler(abstractMapper));
        handlers.add(new EnumTypeHandler(abstractMapper));
        handlers.add(new ExistingObjectHandler());
        handlers.add(new PolymorphicTypeHandler(abstractMapper));
    }

    /**
     * Resolves and handles the property by finding the appropriate handler.
     *
     * @param schemaName         name of the containing schema
     * @param variableProperties target field container
     * @param currentSchema      full schema map
     * @param schemas            global schemas map
     * @param propertyName       field name
     * @param propertiesMap      raw property definition
     * @param processContext     generation context
     * @param innerSchemas       accumulator for inner schemas
     * @return true if a handler was found and executed, false otherwise
     */
    public boolean resolve(String schemaName,
                          VariableProperties variableProperties,
                          Map<String, Object> currentSchema,
                          Map<String, Object> schemas,
                          String propertyName,
                          Map<String, Object> propertiesMap,
                          ProcessContext processContext,
                          Map<String, Object> innerSchemas) {

        for (PropertyTypeHandler handler : handlers) {
            if (handler.canHandle(schemaName, variableProperties, currentSchema, schemas,
                    propertyName, propertiesMap, processContext, innerSchemas)) {
                handler.handle(schemaName, variableProperties, currentSchema, schemas,
                        propertyName, propertiesMap, processContext, innerSchemas);
                return true;
            }
        }

        return false; // No handler found
    }

    /**
     * Adds a custom handler to the chain.
     *
     * @param handler the handler to add
     */
    public void addHandler(PropertyTypeHandler handler) {
        handlers.add(handler);
    }

    /**
     * Gets the list of handlers (mainly for testing).
     *
     * @return the list of handlers
     */
    protected List<PropertyTypeHandler> getHandlers() {
        return handlers;
    }
}
