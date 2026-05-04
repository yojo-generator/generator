package ru.yojo.codegen.parser;

import java.util.Map;

/**
 * Holds the parsed specification data (facade result).
 * Contains the root document, collected schemas, and messages.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SpecificationModel {

    private final Map<String, Object> rootDoc;
    private final Map<String, Object> schemas;
    private final Map<String, Object> messages;

    /**
     * Creates a new specification model.
     *
     * @param rootDoc  the root AsyncAPI document
     * @param schemas  all collected schemas (including from $ref files)
     * @param messages all collected messages (including from $ref files)
     */
    public SpecificationModel(Map<String, Object> rootDoc,
                            Map<String, Object> schemas,
                            Map<String, Object> messages) {
        this.rootDoc = schemas;
        this.schemas = schemas;
        this.messages = messages;
    }

    /**
     * Returns the root AsyncAPI document.
     *
     * @return the root document
     */
    public Map<String, Object> getRootDoc() {
        return rootDoc;
    }

    /**
     * Returns all collected schemas.
     *
     * @return map of schema name → schema definition
     */
    public Map<String, Object> getSchemas() {
        return schemas;
    }

    /**
     * Returns all collected messages.
     *
     * @return map of message name → message definition
     */
    public Map<String, Object> getMessages() {
        return messages;
    }

    /**
     * Returns the number of schemas collected.
     *
     * @return schema count
     */
    public int getSchemaCount() {
        return schemas.size();
    }

    /**
     * Returns the number of messages collected.
     *
     * @return message count
     */
    public int getMessageCount() {
        return messages.size();
    }
}
