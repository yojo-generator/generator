# Yojo Generator — AGENTS.md

**Generated:** 2026-05-15
**Stack:** Java 17, Gradle, JReleaser, JUnit 5

## Overview

AsyncAPI/OpenAPI YAML → Java DTO generator. Parses AsyncAPI v2.0/v2.6/v3.0 specs and produces clean, production-ready Java classes with Lombok, Jackson, and Bean Validation support.

## Project Structure

```
generator/
├── src/main/java/ru/yojo/codegen/
│   ├── generator/       # Code generation engine (entry: YojoGenerator)
│   │   ├── code/        # AbstractCodeGenerator + SchemaCodeGenerator
│   │   └── template/    # Mustache/Freemarker templates
│   ├── domain/          # Domain models (LombokProperties, BuilderProperties, etc.)
│   ├── mapper/          # Schema → Java type mapping (SchemaMapper, MessageMapper)
│   ├── parser/          # YAML parsing & $ref resolution
│   ├── util/            # LombokUtils, StringUtils, etc.
│   ├── constants/       # Dictionary.java (YAML key constants)
│   └── context/         # SpecificationProperties (per-spec config)
├── src/test/
│   ├── java/            # Unit tests
│   └── resources/       # Test YAMLs + expected outputs
├── build.gradle         # Version: 4.5.0, JReleaser config
└── AGENTS.md            # This file
```

## Key Classes

| Class | Path | Role |
|-------|------|------|
| `YojoGenerator` | `generator/YojoGenerator.java` | Main orchestrator |
| `AbstractCodeGenerator` | `generator/code/AbstractCodeGenerator.java` | Base code generation logic |
| `SchemaCodeGenerator` | `generator/code/SchemaCodeGenerator.java` | Per-schema code gen |
| `LombokProperties` | `domain/lombok/LombokProperties.java` | Lombok annotation config |
| `BuilderProperties` | `domain/lombok/BuilderProperties.java` | Builder pattern config |
| `SchemaMapper` | `mapper/SchemaMapper.java` | YAML schema → Java type |
| `Dictionary` | `constants/Dictionary.java` | YAML attribute key constants |

## Conventions

- **Code style:** Java 17, no records, no sealed classes (yet)
- **Testing:** Expected-file based (src/test/resources/example/expected/)
- **Versioning:** Semantic, in `build.gradle` + `Version.kt`
- **CI:** GitHub Actions (`pr-check.yml`, `release.yml`)

## Commands

```bash
./gradlew test           # Run tests
./gradlew build          # Full build
./gradlew jreleaserConfig  # Validate JReleaser config
```

## AI Notes

- Project publishes to Maven Central via JReleaser
- Gradle plugin wraps this core library
- YamlBean/YamlSnake for YAML parsing
- Jackson annotations generated via discriminator support
