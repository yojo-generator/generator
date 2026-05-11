package ru.yojo.codegen.mapper;

/**
 * Strategy interface for handling different types of YAML property definitions.
 * Each handler is responsible for detecting if it can handle a given property
 * and then populating the appropriate fields in VariableProperties.
 *
 * <p>This follows the Chain of Responsibility pattern where handlers are
 * evaluated in order until one accepts the property.</p>
 *
 * <p>Parameters are bundled into {@link PropertyResolutionContext} to keep the
 * interface clean and make it easier to add new parameters in the future.</p>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public interface PropertyTypeHandler {

    /**
     * Checks if this handler can process the given property definition.
     *
     * @param ctx bundled context with all parameters
     * @return true if this handler can handle the property
     */
    boolean canHandle(PropertyResolutionContext ctx);

    /**
     * Handles the property by populating VariableProperties with the appropriate
     * type, imports, and other metadata.
     *
     * @param ctx bundled context with all parameters
     */
    void handle(PropertyResolutionContext ctx);
}
