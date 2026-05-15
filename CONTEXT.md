# Yojo Generator

**AsyncAPI YAML → Java DTO Generator**
https://github.com/yojo-generator/generator

## What

Java library that parses AsyncAPI v2.0/v2.6/v3.0 specifications and generates:
- Java POJOs with Lombok (`@Data`, `@Builder`, `@Value`, etc.)
- Jackson annotations for polymorphism (`@JsonTypeInfo`, `@JsonSubTypes`)
- Bean Validation annotations (`@NotBlank`, `@Min`, `@Pattern`, etc.)
- Manual `toString()`, `equals()`, `hashCode()` when Lombok disabled
- Builder pattern (Lombok `@Builder` or manual Builder class)

## Key features
- 30+ YAML→Java type mappings (UUID, LocalDate, BigDecimal, Map, etc.)
- Polymorphism via `oneOf`/`allOf`
- Discriminator → `@JsonTypeInfo` + `@JsonSubTypes` with `const` support
- Enums with `x-enumNames` (descriptions) and `x-enumValues` (wire values)
- Per-schema Lombok override via `x-lombok`
- `x-final` immutable fields
- Custom class/field annotations via `x-class-annotation`/`x-field-annotation`

## Published
- **Maven Central:** `io.github.yojo-generator:generator`
- **Gradle Plugin:** `io.github.yojo-generator.gradle-plugin`
