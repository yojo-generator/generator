package ru.yojo.codegen.parser;

import java.util.Map;

/**
 * Immutable result of parsing an AsyncAPI specification file.
 *
 * @param rootDoc         raw parsed YAML document
 * @param processedContent preprocessed YAML content (with injected name/package for $ref resolution)
 * @param schemas         collected schemas from components.schemas and external $ref files
 * @param messages        collected/resolved messages from components.messages, channels, or operations
 * @param asyncApiV3      {@code true} if the specification is AsyncAPI 3.x
 */
public record ParseResult(
        Map<String, Object> rootDoc,
        String processedContent,
        Map<String, Object> schemas,
        Map<String, Object> messages,
        boolean asyncApiV3
) {
}
