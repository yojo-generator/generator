package ru.yojo.codegen.domain;

/**
 * Enum representing the available validation API frameworks for generated Jakarta EE / Java EE annotations.
 * <p>
 * Used to select between {@code javax.validation} (Java EE / Spring Boot &lt; 3.x) and
 * {@code jakarta.validation} (Jakarta EE / Spring Boot &gt;= 3.x) import declarations.
 * <p>
 * This replaces the legacy {@code springBootVersion} string-based heuristic.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public enum ValidationApi {

    /**
     * {@code javax.validation.*} — legacy Java EE namespace.
     * Used with Spring Boot versions before 3.x.
     */
    JAVAX,

    /**
     * {@code jakarta.validation.*} — new Jakarta EE namespace.
     * Used with Spring Boot 3.x and later.
     */
    JAKARTA
}
