package ru.yojo.codegen_new;

import java.util.List;
import java.util.Set;

/**
 * Record для представления сообщения
 */
public record Message(
        String messageName,
        String packageName,
        String summary,
        List<Field> fields,
        Set<String> imports
) {
    public Message {
        fields = fields != null ? fields : List.of();
        imports = imports != null ? imports : Set.of();
    }
}