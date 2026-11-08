# Yojo Generator — AGENTS.md

**Updated:** 2026-11-08
**Stack:** Java 17, Gradle 7.6, JReleaser 1.14, JUnit 5

## Overview

AsyncAPI/OpenAPI YAML → Java DTO generator. Parses AsyncAPI v2.0/v2.6/v3.0 specs and produces clean, production-ready Java classes with Lombok, Jackson, and Bean Validation support.

## Project Structure

```
generator/
├── src/main/java/ru/yojo/codegen/
│   ├── generator/       # Code generation engine (entry: YojoGenerator)
│   │   └── code/        # AbstractCodeGenerator, SchemaCodeGenerator, MessageCodeGenerator
│   ├── domain/          # Domain models (VariableProperties, Schema, Message, LombokProperties, ...)
│   ├── mapper/          # YAML → Java mapping (AbstractMapper, SchemaMapper, MessageMapper, handlers)
│   ├── parser/          # YAML parsing & $ref resolution
│   ├── context/         # YojoContext, ProcessContext, SpecificationProperties
│   ├── constants/       # Dictionary.java (YAML keys, imports, format maps)
│   ├── exception/       # SchemaFillException
│   ├── util/            # LombokUtils, MapperUtil, StringUtils, Logger
│   └── writer/          # JavaFileWriter
├── src/test/
│   ├── java/            # Unit tests
│   └── resources/       # Contract YAMLs + expected Java output
├── build.gradle         # Version + JReleaser config
├── CHANGELOG.md     # Keep a Changelog format
└── AGENTS.md            # This file
```

## Key Classes

| Class | Path | Role |
|-------|------|------|
| `YojoGenerator` | `generator/YojoGenerator.java` | Main orchestrator |
| `AbstractCodeGenerator` | `generator/code/AbstractCodeGenerator.java` | Base code generation logic |
| `SchemaCodeGenerator` | `generator/code/SchemaCodeGenerator.java` | Per-schema code gen |
| `MessageCodeGenerator` | `generator/code/MessageCodeGenerator.java` | Per-message code gen |
| `AbstractMapper` | `mapper/AbstractMapper.java` | Shared property mapping (`$ref`, enums, maps, Lombok annotations) |
| `SchemaMapper` | `mapper/SchemaMapper.java` | YAML schema → Java type |
| `MessageMapper` | `mapper/MessageMapper.java` | Message payload mapping, `x-remove-schema` cleanup |
| `LombokProperties` | `domain/lombok/LombokProperties.java` | Lombok annotation config |
| `BuilderProperties` | `domain/lombok/BuilderProperties.java` | Builder pattern config |
| `DateTimeType` | `domain/DateTimeType.java` | `OFFSET_DATE_TIME` / `INSTANT` for `format: date-time` |
| `Dictionary` | `constants/Dictionary.java` | YAML attribute key constants |

## Conventions

- **Code style:** Java 17, no records, no sealed classes (yet)
- **Testing:** Contract YAMLs in `src/test/resources/example/contract/`, generated sources asserted as text and compiled in tests
- **Versioning:** Semantic, in `build.gradle`; changelog in `CHANGELOG.md`
- **CI:** GitHub Actions (`pr-check.yml` on PRs and pushes to master, `release.yml` on tag push)
- **Toolchain:** builds require JDK 17 — Gradle wrapper 7.6 does not support JDK 21

## Commands

```bash
./gradlew test           # Run tests
./gradlew build          # Full build
./gradlew jreleaserConfig  # Validate JReleaser config (needs build/jreleaser dir + secrets)
```

## AI Notes

- Project publishes to Maven Central via JReleaser on tag push; the tag and GitHub Release are created manually (`skipRelease`/`skipTag` are enabled in `build.gradle`)
- Gradle plugin wraps this core library and exposes `validationApi`, `dateTimeType` and Lombok settings
- YamlBean/YamlSnake for YAML parsing
- Jackson annotations generated via discriminator support
