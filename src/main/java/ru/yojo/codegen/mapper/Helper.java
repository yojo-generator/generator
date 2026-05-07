package ru.yojo.codegen.mapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared mutable state container used during schema/message mapping.
 * <p>
 * Tracks:
 * <ul>
 *   <li>Which schemas should be excluded from generation (e.g., referenced via {@code $ref})</li>
 *   <li>Inner schemas discovered during processing (e.g., enums, nested DTOs)</li>
 *   <li>Context flags for mapper logic (e.g., mapping origin: messages vs schemas)</li>
 * </ul>
 * <p>
 * Instances are typically held in {@link ru.yojo.codegen.context.ProcessContext} and shared
 * across {@link ru.yojo.codegen.mapper.SchemaMapper} and {@link ru.yojo.codegen.mapper.MessageMapper}.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class Helper {

    /**
     * {@code true} if current mapping phase originates from message processing.
     */
    private Boolean isMappedFromMessages = false;

    /**
     * {@code true} if current mapping phase originates from schema processing.
     */
    private Boolean isMappedFromSchemas = false;

    /**
     * Set of schema names to be *removed* after mapping (e.g., when {@code removeSchema: true} is specified).
     */
    private Set<String> removeSchemas = new HashSet<>();

    /**
     * Set of schema names that should *not* be removed, even if present in {@link #removeSchemas}
     * (e.g., schemas referenced from multiple places).
     */
    private Set<String> excludeRemoveSchemas = new HashSet<>();

    /**
     * Set of schema names excluded from DTO field filling due to inheritance (e.g., {@code extends: SchemaName}).
     */
    private Set<String> excludeInheritanceSchemas = new HashSet<>();

    /**
     * Accumulator for inner schemas discovered during mapping (e.g., enums, inner DTOs).
     * Key: schema name, Value: schema definition map.
     */
    private Map<String, Object> innerSchemas = new ConcurrentHashMap<>();

    /**
     * Returns whether the current context is mapping from messages.
     *
     * @return {@code true} if mapping originates from message processing
     */
    public Boolean isMappedFromMessages() {
        return isMappedFromMessages;
    }

    /**
     * Sets the flag indicating mapping originates from messages.
     *
     * @param isMappedFromMessages flag value
     */
    public void setIsMappedFromMessages(Boolean isMappedFromMessages) {
        this.isMappedFromMessages = isMappedFromMessages;
    }

    /**
     * Returns whether the current context is mapping from schemas.
     *
     * @return {@code true} if mapping originates from schema processing
     */
    public Boolean isMappedFromSchemas() {
        return isMappedFromSchemas;
    }

    /**
     * Sets the flag indicating mapping originates from schemas.
     *
     * @param isMappedFromSchemas flag value
     */
    public void setIsMappedFromSchemas(Boolean isMappedFromSchemas) {
        this.isMappedFromSchemas = isMappedFromSchemas;
    }

    /**
     * Returns the set of schema names marked for removal after processing.
     *
     * @return mutable set of schema names
     */
    public Set<String> getRemoveSchemas() {
        return removeSchemas;
    }

    /**
     * Sets the set of schema names to be removed.
     *
     * @param removeSchemas set of schema names
     */
    public void setRemoveSchemas(Set<String> removeSchemas) {
        this.removeSchemas = removeSchemas;
    }

    /**
     * Returns the set of schema names to be preserved (excluded from removal).
     *
     * @return mutable set of schema names
     */
    public Set<String> getExcludeRemoveSchemas() {
        return excludeRemoveSchemas;
    }

    /**
     * Sets the set of schema names to exclude from removal.
     *
     * @param excludeRemoveSchemas set of schema names
     */
    public void setExcludeRemoveSchemas(Set<String> excludeRemoveSchemas) {
        this.excludeRemoveSchemas = excludeRemoveSchemas;
    }

    /**
     * Returns the set of schema names excluded from field population due to inheritance.
     *
     * @return mutable set of schema names
     */
    public Set<String> getExcludeInheritanceSchemas() {
        return excludeInheritanceSchemas;
    }

    /**
     * Sets the set of schema names to exclude from inheritance-based field filling.
     *
     * @param excludeInheritanceSchemas set of schema names
     */
    public void setExcludeInheritanceSchemas(Set<String> excludeInheritanceSchemas) {
        this.excludeInheritanceSchemas = excludeInheritanceSchemas;
    }

    /**
     * Returns the map of inner schemas discovered during mapping.
     *
     * @return concurrent map: schema name → schema definition
     */
    public Map<String, Object> getInnerSchemas() {
        return innerSchemas;
    }

    /**
     * Sets the map of inner schemas.
     *
     * @param innerSchemas map of inner schemas
     */
    public void setInnerSchemas(Map<String, Object> innerSchemas) {
        this.innerSchemas = innerSchemas;
    }
}