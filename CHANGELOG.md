# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.7.0] - 2026-11-08

### Added

- `x-masked` attribute to hide sensitive values in the generated `toString()`:
  `true` replaces the value with `***`, `false` leaves it as is, and a
  fully-qualified static method name is used as a custom masker. With Lombok the
  field is excluded from the generated `toString()` and a `toString<FieldName>()
  method is added instead. Masking a whole collection or map field is supported;
  `x-masked` inside `items` or `additionalProperties` is rejected with
  `SchemaFillException`. Enum fields are never masked, because their constants are
  not sensitive. (#81)
- `DateTimeType` setting (`OFFSET_DATE_TIME` or `INSTANT`) for `format: date-time`,
  exposed through `YojoContext`, `ProcessContext`, `VariableProperties` and the CLI
  (seventh argument). Default stays `OffsetDateTime`. (#87)
- Class-level `x-json-include` is now also read from message payloads, not only from
  schemas. (#83)

### Fixed

- Referenced enums declared with a primitive `type` no longer produce a duplicate
  enum type or drop the `$ref` attributes. (#89)
- A bare `default` value on an enum field is qualified with the enum type
  (`UserStatus.ACTIVE` instead of `= ACTIVE;`), so the generated code compiles. (#89)
- `@Valid` is no longer emitted on enum fields in message payloads. (#89)
- `x-remove-schema` no longer generates an empty message class: messages are mapped
  once and the already mapped list is written out. (#85)
- CLI no longer fails with `NoSuchFileException`/`NullPointerException` when the
  specification is passed as a bare file name; the specification name keeps its
  extension, so the input directory resolves correctly.
