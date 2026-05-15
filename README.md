# Yojo — AsyncAPI → Java DTO Generator

[![Maven Central](https://img.shields.io/maven-central/v/io.github.yojo-generator/generator.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.yojo-generator/generator)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Gradle%20Plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fio%2Fgithub%2Fyojo-generator%2Fgradle-plugin%2Fio.github.yojo-generator.gradle-plugin.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/io.github.yojo-generator.gradle-plugin)
[![JDK 17+](https://img.shields.io/badge/JDK-17%2B-green.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE.md)
[![AsyncAPI](https://img.shields.io/badge/AsyncAPI-2.0%2F2.6%2F3.0-blue)](https://www.asyncapi.com/)
[![Javadoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://javadoc.io/doc/io.github.yojo-generator/generator)
![Build](https://img.shields.io/github/actions/workflow/status/yojo-generator/generator/build.yml?branch=develop)

**Java DTO generator for AsyncAPI** — transforms YAML specifications into production-ready Java POJOs. Supports Lombok annotations (`@Data`, `@Builder`, `@Value`, `@Slf4j`), Jackson polymorphism (`@JsonTypeInfo`, `@JsonSubTypes`), Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Pattern`), and manual `toString`/`equals`/`hashCode`. Works with AsyncAPI v2.0/v2.6/v3.0 and schema-driven YAML contracts.

---

## Table of Contents

- [Quick Start](#quick-start)
- [Supported Specifications](#supported-specifications)
- [Installation](#installation)
- [Usage Examples](#usage-examples)
- [YAML ↔ Java Type Mapping](#yaml--java-type-mapping)
- [Supported Attributes](#supported-attributes)
- [Security](#security)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Quick Start

### 1. Add the Gradle plugin

```gradle
plugins {
    id("io.github.yojo-generator.gradle-plugin") version "<latest>"
}
```

### 2. Configure

```gradle
yojo {
    configurations {
        create("main") {
            specificationProperties {
                register("api") {
                    specName = "order-service.yaml"
                    inputDirectory = file("contract").absolutePath
                    outputDirectory = layout.buildDirectory
                        .dir("generated/sources/yojo/com/example/api").get().asFile.absolutePath
                    packageLocation = "com.example.api"
                }
            }
        }
    }
}
```

### 3. Run

```bash
./gradlew generateClasses
```

Generated DTOs appear in `build/generated/sources/yojo/`.

---

## Supported Specifications

| Specification            | Status          | Notes                                                                   |
|--------------------------|-----------------|-------------------------------------------------------------------------|
| **AsyncAPI v2.0 / v2.6** | ✅ Full          | Primary target — all features supported                                 |
| **AsyncAPI v3.0 (RC)**   | ✅ Experimental  | `operations`, `channels`, `messages`; `payload: { schema: {...} }`      |
| **OpenAPI 3.x**          | ❌ Not supported | Consider [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) |

---

## Installation

### Gradle (recommended)

```gradle
plugins {
    id("io.github.yojo-generator.gradle-plugin") version "<latest>"
}
```

> 🔗 [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.yojo-generator.gradle-plugin) · [Plugin source](https://github.com/yojo-generator/gradle-plugin)

### Maven

```xml
<dependency>
    <groupId>io.github.yojo-generator</groupId>
    <artifactId>generator</artifactId>
    <version><latest></version>
</dependency>
```

<details>
<summary>📦 Full Gradle configuration example</summary>

```gradle
yojo {
    configurations {
        create("main") {
            specificationProperties {
                register("api") {
                    specName("test.yaml")
                    inputDirectory(layout.projectDirectory.dir("contract").asFile.absolutePath)
                    outputDirectory(layout.buildDirectory.dir("generated/sources/yojo/com/example/api")
                        .get().asFile.absolutePath)
                    packageLocation("com.example.api")
                }
                register("one-more-api") {
                    specName("test.yaml")
                    inputDirectory(layout.projectDirectory.dir("contract").asFile.absolutePath)
                    outputDirectory(layout.buildDirectory.dir("generated/sources/yojo/oneMoreApi")
                        .get().asFile.absolutePath)
                    packageLocation("oneMoreApi")
                }
            }
            springBootVersion("3.2.0")
            lombok {
                enable(true)
                allArgsConstructor(true)
                noArgsConstructor(true)
                accessors {
                    enable(true)
                    fluent(false)
                    chain(true)
                }
                equalsAndHashCode {
                    enable(true)
                    callSuper(false)
                }
                builder {
                    enable(true)
                    singular(true)
                    builderDefault(true)
                }
            }
        }
    }
}
```
</details>

---

## Usage Examples

### Collections

```yaml
listOfLongs:
  type: array
  items:
    type: integer
    format: int64
    x-realization: ArrayList
setOfDates:
  type: array
  format: set
  items:
    type: string
    format: date
    x-realization: HashSet
```

```java
private List<Long> listOfLongs = new ArrayList<>();
private Set<LocalDate> setOfDates = new HashSet<>();
```

> ⚠️ The old `realization` key is deprecated. Use `x-realization` instead.

---

### Maps with UUID keys

```yaml
mapUUIDCustomObject:
  type: object
  format: uuid
  additionalProperties:
    $ref: '#/components/schemas/User'
```

```java
private Map<UUID, User> mapUUIDCustomObject;
```

---

### Enums with descriptions (`x-enumNames`)

```yaml
Result:
  type: object
  enum:
    - SUCCESS
    - DECLINE
    - error-case
  x-enumNames:
    SUCCESS: "Operation succeeded"
    DECLINE: "Declined by policy"
    error-case: "Legacy lowercase"
```

```java
public enum Result {
    SUCCESS("Operation succeeded"),
    DECLINE("Declined by policy"),
    ERROR_CASE("Legacy lowercase");

    private final String value;
    Result(String value) { this.value = value; }
    public String getValue() { return value; }
}
```

> ✅ Automatic naming: `error-case` → `ERROR_CASE`, `class` → `CLASS_FIELD`

---

### Enums with wire values (`x-enumValues`)

```yaml
OrderStatus:
  type: object
  enum:
    - PENDING
    - CONFIRMED
    - CANCELLED
  x-enumValues:
    PENDING: "P"
    CONFIRMED: "C"
    CANCELLED: "X"
```

```java
public enum OrderStatus {
    PENDING("P"),
    CONFIRMED("C"),
    CANCELLED("X");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for (OrderStatus v : OrderStatus.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
```

> Generates `@JsonValue` on the getter and `@JsonCreator` static factory for Jackson serialization/deserialization.

### Enum default fallback (`x-enumDefault`)

When combined with `x-enumValues`, setting `x-enumDefault: true` adds an `UNKNOWN_DEFAULT_YOJO` fallback constant. Instead of throwing `IllegalArgumentException`, the `fromValue()` method returns this sentinel for unmapped wire values:

```yaml
StatusWithDefault:
  type: object
  enum:
    - STARTED
    - STOPPED
  x-enumValues:
    STARTED: "S"
    STOPPED: "T"
  x-enumDefault: true
```

```java
public enum StatusWithDefault {
    STARTED("S"),
    STOPPED("T"),
    UNKNOWN_DEFAULT_YOJO("UNKNOWN");   // ← fallback

    // ... @JsonValue, @JsonCreator as above ...

    @JsonCreator
    public static StatusWithDefault fromValue(String value) {
        for (StatusWithDefault v : StatusWithDefault.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        return UNKNOWN_DEFAULT_YOJO;   // ← no exception thrown
    }
}
```

---

### BigDecimal with `@Digits` (prefer `multipleOf`)

```yaml
bigDecimalValue:
  type: number
  format: big-decimal
  multipleOf: 0.01   # → fraction = 2
bigDecimalValue2:
  type: number
  format: big-decimal
  multipleOf: 10.0   # → integer = 2, fraction = 1
```

```java
@Digits(integer = 1, fraction = 2)
private BigDecimal bigDecimalValue;

@Digits(integer = 2, fraction = 1)
private BigDecimal bigDecimalValue2;
```

> ⚠️ `digits: "integer = 2, fraction = 2"` is legacy. Prefer `multipleOf` — it is standards-compliant.  
> Use `x-digits` if you need the explicit form (see [Supported Attributes](#supported-attributes)).

---

### Polymorphism (`oneOf`, `allOf`)

```yaml
polymorph:
  oneOf:
    - $ref: '#/components/schemas/StatusOnly'
    - $ref: '#/components/schemas/StatusWithCode'
```

```java
public class PolymorphStatusOnlyStatusWithCode {
    private String status;
    private Integer code;
}
```

---

### Discriminator with `const` support

```yaml
Pet:
  type: object
  discriminator: petType
  properties:
    name:
      type: string
    petType:
      type: string

Cat:
  allOf:
    - $ref: '#/components/schemas/Pet'
  properties:
    huntingSkill:
      type: string

Dog:
  allOf:
    - $ref: '#/components/schemas/Pet'
  properties:
    packSize:
      type: integer

StickInsect:
  allOf:
    - $ref: '#/components/schemas/Pet'
    - type: object
      properties:
        petType:
          const: StickBug   # ← Overrides default discriminator value
        color:
          type: string
```

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
              property = "petType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cat.class, name = "Cat"),
    @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
    @JsonSubTypes.Type(value = StickInsect.class, name = "StickBug")
})
public class Pet {
    private String name;
    @JsonTypeId
    private String petType;
}

public class Cat extends Pet {
    private String huntingSkill;
}

public class Dog extends Pet {
    private Integer packSize;
}

public class StickInsect extends Pet {
    private String color;   // petType inherited from Pet, not duplicated
}
```

---

### Builder pattern (`@Builder` / manual Builder)

Yojo supports the builder pattern in two modes:

**With Lombok** — generates `@Builder`, `@Singular`, and `@Builder.Default` annotations.
**Without Lombok** — generates a full static inner `Builder` class with fluent setters, singular adders, and `build()` method.

#### DSL configuration (build.gradle)

```groovy
lombok {
    builder {
        enable = true        // Generate @Builder or manual Builder class
        singular = true      // Add @Singular/adder methods for List/Set fields
        builderDefault = true // Apply @Builder.Default to fields with default values
    }
}
```

#### YAML schema-level override (`x-lombok`)

```yaml
MyDto:
  type: object
  x-lombok:
    builder:
      enable: true
      singular: true
      builderDefault: true
  properties:
    names:
      type: array
      items:
        type: string
      description: Gets @Singular("name")
    scores:
      type: array
      format: set
      items:
        type: integer
      description: Gets @Singular("score")
    message:
      type: string
      default: Hello
      description: Gets @Builder.Default
    count:
      type: integer
      description: Regular field (no builder annotation)
```

#### Generated output (with Lombok)

```java
@Builder
public class MyDto {

    @Singular("name")
    private List<String> names;

    @Singular("score")
    private Set<Integer> scores;

    @Builder.Default
    private String message = "Hello";

    private Integer count;
}
```

#### Generated output (without Lombok)

```java
public class MyDto {

    private List<String> names;
    private Set<Integer> scores;
    private String message = "Hello";
    private Integer count;

    // getters/setters ...

    private MyDto(Builder builder) {
        this.names = builder.names;
        this.scores = builder.scores;
        this.message = builder.message;
        this.count = builder.count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> names = new ArrayList<>();
        private Set<Integer> scores = new HashSet<>();
        private String message = "Hello";
        private Integer count;

        public Builder names(List<String> names) { ... return this; }
        public Builder name(String name) { this.names.add(name); return this; }  // singular
        public Builder score(Integer score) { this.scores.add(score); return this; }
        public MyDto build() { return new MyDto(this); }
    }
}
```

> 💡 The singular name is derived automatically by removing the trailing `s` from the field name
> (e.g., `names` → `"name"`, `scores` → `"score"`). This matches Lombok's `@Singular` convention.

#### Builder DSL reference

| Option | Type | Default | Description |
|---|---|---|---|
| `enable` | `boolean` | `false` | Generate `@Builder` (Lombok) or manual Builder class (no Lombok) |
| `singular` | `boolean` | `true` | Add `@Singular` / singular adder methods for `List`/`Set` fields |
| `builderDefault` | `boolean` | `true` | Apply `@Builder.Default` / propagate default values to Builder |

---

### Custom annotations (`x-class-annotation`, `x-field-annotation`)

| Attribute | YAML | → Java |
|---|---|---|
| `x-class-annotation` | <pre lang="yaml">x-class-annotation:<br>  - com.example.MyAnnotation<br>  - com.example.Another("param")</pre> | <pre lang="java">@MyAnnotation<br>@Another("param")<br>public class MySchema { ... }</pre> |
| `x-field-annotation` | <pre lang="yaml">myField:<br>  type: string<br>  x-field-annotation:<br>    - com.example.MyFieldAnnotation</pre> | <pre lang="java">@MyFieldAnnotation<br>private String myField;</pre> |

---

### Final fields (`x-final`)

```yaml
ImmutableDto:
  type: object
  properties:
    createdAt:
      type: string
      format: date-time
      x-final: true
    name:
      type: string
```

```java
@Generated("Yojo")
public class ImmutableDto {

    public ImmutableDto(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private final OffsetDateTime createdAt;

    private String name;

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

> Fields marked with `x-final: true` are declared as `final` and must be initialized via constructor. A constructor is generated automatically for all final fields without default values. Lombok's `@NoArgsConstructor` is skipped when uninitialized final fields exist.

---

## YAML ↔ Java Type Mapping

### Scalar types

| YAML                                                                | Java              | Imports                                              |
|---------------------------------------------------------------------|-------------------|------------------------------------------------------|
| `type: string`, `format: uuid`                                      | `UUID`            | `java.util.UUID`                                     |
| `type: object`, `format: date`                                      | `LocalDate`       | `java.time.LocalDate`                                |
| `type: string`, `format: local-date-time`                           | `LocalDateTime`   | `java.time.LocalDateTime`                            |
| `type: object`, `format: date-time`                                 | `OffsetDateTime`  | `java.time.OffsetDateTime`                           |
| `type: number`, `format: big-decimal`, `multipleOf: 0.01`           | `BigDecimal`      | `java.math.BigDecimal`, `@Digits(integer = 1, fraction = 2)` |
| `type: string`, `format: email`                                     | `String`          | `@Email` (Jakarta/javax)                             |
| `type: string`, `format: uri`                                       | `URI`             | `java.net.URI`                                       |
| `type: integer`, `format: int64`                                    | `Long`            | —                                                    |
| `type: number`, `format: float`                                     | `Float`           | —                                                    |
| `type: boolean`                                                     | `Boolean`         | —                                                    |

### Collections & maps

| YAML | → Java |
|------|--------|
| <pre lang="yaml">type: array<br>items:<br>  type: string</pre> | `List<String>` |
| <pre lang="yaml">type: array<br>format: set<br>items:<br>  type: integer</pre> | `Set<Integer>` |
| <pre lang="yaml">type: object<br>additionalProperties:<br>  type: string</pre> | `Map<String, String>` |
| <pre lang="yaml">type: object<br>format: uuid<br>additionalProperties:<br>  $ref: '#/...'</pre> | `Map<UUID, User>` |
| <pre lang="yaml">type: object<br>additionalProperties:<br>  type: array<br>  format: set<br>  items:<br>    type: string</pre> | `Map<String, Set<String>>` |

---

## Supported Attributes

All custom Yojo attributes now have `x-` prefixed equivalents. While the old names still work (backward compatibility), they are **deprecated** and will log a warning. New contracts should use the `x-` prefixed names.

| Attribute (old, deprecated) | Attribute (new, preferred) | Scope | Type | Example | Effect |
|---|---|---|---|---|---|
| — | `x-realization` | `items`, `additionalProperties` | `string` | `ArrayList` | `= new ArrayList<>()` |
| `realization` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-digits` | number | `string` | `"integer=2, fraction=2"` | `@Digits(...)` |
| `digits` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-additional-format` | `additionalProperties` | `string` | `uuid` | Custom map key type |
| `additionalFormat` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-validation-groups` | schema | `list` | `[Create.class]` | `@NotBlank(groups = {Create.class})` |
| `validationGroups` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-validation-groups-imports` | schema | `list` | `[pkg.Validation]` | `import pkg.Validation;` |
| `validationGroupsImports` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-validate-by-groups` | schema | `list` | `[name, email]` | Annotations only on listed fields |
| `validateByGroups` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-lombok.*` | schema/message | nested | *see Gradle config* | `@Data`, `@Accessors`, etc. |
| `lombok.*` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-extends` / `x-implements` | schema/message | map | `x-from-class`, `x-from-package` | `extends X implements Y` |
| `extends` / `implements` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-from-class` | inside `x-extends` | `string` | `BaseClass` | Superclass name |
| `fromClass` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-from-package` | inside `x-extends` | `string` | `com.example` | Superclass package |
| `fromPackage` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-from-interface` | inside `x-implements` | `list` | `[com.example.Ifc]` | Interface FQN list |
| `fromInterface` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-remove-schema` | `message.payload` | `boolean` | `true` | Skip DTO generation for `$ref` target |
| `removeSchema` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-methods` | schema (interface) | map | *see [interface example](#custom-annotations-x-class-annotation-x-field-annotation)* | Interface method definitions |
| `methods` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-imports` | schema (interface) | `list` | `[java.util.List]` | Interface-level imports |
| `imports` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-definition` | inside method entry | `string` | `"void doWork();"` | Method signature in interfaces |
| `definition` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| — | `x-path-for-generate-message` | `message.payload` | `string` | `io.github.events` | Custom message package |
| `pathForGenerateMessage` | — | ↑ | ↑ | ↑ | ↑ (deprecated) |
| `primitive` | — | field | `boolean` | `true` | `int`, `boolean`, `long` |
| `multipleOf` | — | number | `number` | `0.01` | `@Digits(integer = 1, fraction = 2)` |
| `const` | — | schema, field | `string` | `"StickBug"` | Override discriminator value |
| `discriminator` | — | schema | `string` | `"petType"` | `@JsonTypeInfo` + `@JsonSubTypes` |
| `format: interface` | — | schema | — | — | `interface X { ... }` |
| `format: existing` | — | field | `name`, `package` | — | Import existing class |
| `x-enumNames` | — | enum | `map` | `SUCCESS: "Ok"` | Enum with description field |
| `x-enumValues` | — | enum | `map` | `ACTIVE: "A"` | Enum with wire values → `@JsonValue`/`@JsonCreator` |
| `x-enumDefault` | — | enum predicate | `boolean` | `true` | Adds `UNKNOWN_DEFAULT_YOJO` fallback |
| `x-class-annotation` | — | schema, message | `list` | `[com.example.MyAnnotation]` | Class-level annotations |
| `x-field-annotation` | — | field | `list` | `[com.example.MyAnnotation("v")]` | Field-level annotations |
| `x-final` | — | field | `boolean` | `true` | Generates `final` field declaration |

---

## Security

### Path traversal prevention

Yojo validates all file paths when writing generated source code to prevent directory traversal attacks. File names are checked for:
- Path separator characters (`/`, `\`)
- Parent directory references (`..`)
- Normalized path containment within the target directory

If a path traversal attempt is detected, a `SecurityException` is thrown.

> **CVE-2024-45373** — Path traversal vulnerability in `JavaFileWriter` was identified and fixed in commit `609da11`.

### Reporting

If you discover a security issue, please contact the maintainer directly (email below) rather than opening a public issue.

---

## Roadmap

| Feature | Status | Description |
|---|---|---|
| **Discriminator** | ✅ Done | `@JsonTypeInfo`, `@JsonSubTypes` for polymorphism |
| **`@JsonTypeId` on fields** | ✅ Done | Discriminator field annotation in subtypes |
| **Discriminator `const`** | ✅ Done | Override default discriminator value via `const` |
| **Jackson annotations** | 🟡 Partial | `@JsonProperty`, `@JsonInclude`, `@JsonValue`/`@JsonCreator` (via `x-enumValues`) done |
| **x- prefixed attributes** | ✅ Done (4.3.0) | All custom attributes deprecated in favour of `x-` equivalents |
| **AsyncAPI spec validation** | 🚧 Planned | Validate `$ref`, `type`, `format`, detect circular refs |
| **Lombok extensions** | ✅ Done (4.4.0) | `@Builder`, `@Singular`, `@Builder.Default` + manual Builder class |
| **OpenAPI 3.1 support** | 🚧 Planned | After AsyncAPI 3.0 stabilizes |
| **Freemarker templates** | 🚧 Planned | Customizable code generation for enterprises |

---

## Contributing

1. Fork the repository
2. Create a feature branch from `develop`
3. Make changes — each step in a separate branch
4. Run tests: `./gradlew test`
5. Open a Pull Request

### Development requirements

- JDK 17+
- Gradle (wrapper included)

All generated output is tested against expected files in `src/test/resources/example/expected/`.

---

## Contact

- **Author**: Vladimir Morozkin
- **Email**: `jvmorozkin@gmail.com`
- **Telegram**: [@vmorozkin](https://t.me/vmorozkin)
- **GitHub**: [yojo-generator](https://github.com/yojo-generator)

> Got an AsyncAPI contract? Send it over — I'll make a proof of concept.

---

## License

Distributed under the **Apache License 2.0**. See [LICENSE.md](LICENSE.md).

---

**AsyncAPI → Java — fast, precise, zero manual work.**
