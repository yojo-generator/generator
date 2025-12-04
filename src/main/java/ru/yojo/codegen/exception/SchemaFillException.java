package ru.yojo.codegen.exception;

/**
 * Thrown when schema processing fails due to invalid or unsupported configuration in the AsyncAPI contract.
 * <p>
 * Typical causes:
 * <ul>
 *   <li>Missing required attributes (e.g., {@code type} in schema)</li>
 *   <li>Inconsistent validation group configuration</li>
 *   <li>Unresolvable references</li>
 * </ul>
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class SchemaFillException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message descriptive error message
     */
    public SchemaFillException(String message) {
        super(message);
    }
}