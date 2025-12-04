# 🚀 Yojo — AsyncAPI → Java DTO Generator
**YAML-to-POJO for AsyncAPI v2.0/v2.6/v3.0**  
✅ Polymorphism ✅ Validation ✅ Lombok ✅ Collections/Map ✅ Enums with Descriptions ✅ Inheritance/Interfaces  
❌ **OpenAPI is not supported** (AsyncAPI only)

![Yojo Banner](./yojo.png)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.yojo-generator/generator.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.yojo-generator/generator)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Gradle%20Plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fio%2Fgithub%2Fyojo-generator%2Fgradle-plugin%2Fio.github.yojo-generator.gradle-plugin.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/io.github.yojo-generator.gradle-plugin)
[![JDK 17+](https://img.shields.io/badge/JDK-17%2B-green.svg)](https://adoptium.net/)  
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE.md)
![AsyncAPI 2.0+](https://img.shields.io/badge/AsyncAPI-2.0%2F2.6%2F3.0-blue)
[![Documentation](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://javadoc.io/doc/io.github.yojo-generator/generator)  
[![Javadocs](http://www.javadoc.io/badge/io.github.yojo-generator/generator.svg)](http://www.javadoc.io/doc/io.github.yojo-generator/generator)
![GitHub issues](https://img.shields.io/github/issues/yojo-generator/generator)
![GitHub closed issues](https://img.shields.io/github/issues-closed/yojo-generator/generator)
![GitHub pull requests](https://img.shields.io/github/issues-pr/yojo-generator/generator)
![Status](https://img.shields.io/badge/status-active-success)

---

## ⚠️ Important: Specification Support

| Specification            | Status          | Notes                                                                       |
|--------------------------|-----------------|-----------------------------------------------------------------------------|
| **AsyncAPI v2.0 / v2.6** | ✅ Full          | Primary target version                                                      |
| **AsyncAPI v3.0 (RC)**   | ✅ Experimental  | Supports `operations`, `channels`, `messages`; `payload: { schema: {...} }` |
| **OpenAPI 3.x**          | ❌ Not supported | Planned no earlier than 2026                                                |

> 💡 For OpenAPI, consider [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator). Yojo is exclusively focused on **AsyncAPI**.

---

## 🧾 Usage Examples (valid YAML ↔ Java)

All examples use **valid AsyncAPI-compliant YAML** — no abbreviated forms like `items: { type: integer }`.

---

### 1. Primitives + `primitive: true`
**YAML**
```yaml
booleanValueWithRequired:
  type: boolean
  primitive: true
byteValueWithAnnotations:
  type: number
  format: byte
  multipleOf: 1
  minimum: 2
  maximum: 4
```
→ **Java**
```java
private boolean booleanValueWithRequired;

@Max(4)
@Min(2)
@NotNull
@Digits(integer = 1, fraction = 0)  // multipleOf: 1 → fraction = 0
private byte byteValueWithAnnotations;
```

---

### 2. Collections — correct `items` structure
**YAML**
```yaml
listOfLongs:
  type: array
  items:
    type: integer
    format: int64
    realization: ArrayList
setOfDates:
  type: array
  format: set
  items:
    type: string
    format: date
    realization: HashSet
```
→ **Java**
```java
private List<Long> listOfLongs = new ArrayList<>();
private Set<LocalDate> setOfDates = new HashSet<>();
```

---

### 3. Map with UUID keys
**YAML**
```yaml
mapUUIDCustomObject:
  type: object
  format: uuid
  additionalProperties:
    $ref: '#/components/schemas/User'
```
→ **Java**
```java
private Map<UUID, User> mapUUIDCustomObject;
```

---

### 4. Enum with descriptions (`x-enumNames`)
**YAML**
```yaml
Result:
  type: object
  enum: [SUCCESS, DECLINE, error-case, "\r\n"]
  x-enumNames:
    SUCCESS: "Operation succeeded"
    DECLINE: "Declined by policy"
    error-case: "Legacy lowercase"
    "\r\n": "CRLF"
```
→ **Java**
```java
public enum Result {
    SUCCESS("Operation succeeded"),
    DECLINE("Declined by policy"),
    ERROR_CASE("Legacy lowercase"),
    CARRIAGE_RETURN_LINE_FEED("CRLF");

    private final String value;
    Result(String value) { this.value = value; }
    public String getValue() { return value; }
}
```

> ✅ `error-case` → `ERROR_CASE`, `\r\n` → `CARRIAGE_RETURN_LINE_FEED`, `class` → `CLASS_FIELD`

---

### 5. `multipleOf` → `@Digits` (**preferred over `digits`**)
**YAML**
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
→ **Java**
```java
@Digits(integer = 1, fraction = 2)
private BigDecimal bigDecimalValue;

@Digits(integer = 2, fraction = 1)
private BigDecimal bigDecimalValue2;
```

> ⚠️ `digits: "integer = 2, fraction = 2"` is **legacy**. **Prefer `multipleOf`** — it’s standards-compliant, editor-validated, and used for `@Digits` inference.

---

### 6. Polymorphism (`oneOf`, `allOf`)
**YAML**
```yaml
polymorph:
  oneOf:
    - $ref: '#/components/schemas/StatusOnly'        # { status: string }
    - $ref: '#/components/schemas/StatusWithCode'    # { status: string, code: integer }
```
→ **Java**
```java
public class PolymorphStatusOnlyStatusWithCode {
    private String status;   // from both
    private Integer code;    // only from StatusWithCode (nullable)
}
```

---

### 7. Inheritance & Interfaces (complete cases)

#### 7.1 Simple inheritance
**YAML**
```yaml
UserDto:
  type: object
  extends:
    fromClass: BaseEntity
    fromPackage: com.my.base
  properties:
    name: { type: string }
```
→ `class UserDto extends BaseEntity { private String name; }`

#### 7.2 Override in message
**YAML**
```yaml
CreateUserMessage:
  payload:
    $ref: '#/components/schemas/UserDto'
    extends:
      fromClass: BaseEvent
      fromPackage: com.my.events
```
→ `class CreateUserMessage extends BaseEvent { /* fields from UserDto */ }`  
(⚠️ `UserDto` is **not generated** if `UserDto` == `BaseEvent`)

#### 7.3 `extends: SchemaName` — skip field duplication
```yaml
MyDto:
  type: object
  $ref: './User.yaml#/components/schemas/User'
  extends:
    fromClass: User
    fromPackage: com.example.common
```
→ `class MyDto extends User { }` — **no field duplication**.

---

### 8. `realization` — Collections and Maps

| Attribute        | YAML                                                                                                                             | → Java                                                 |
|------------------|----------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------|
| `realization`    | <pre lang="yaml">users:<br>  type: array<br>  items:<br>    $ref: '#/components/schemas/User'<br>  realization: LinkedList</pre> | `private List<User> users = new LinkedList<>();`       |
|                  | <pre lang="yaml">cache:<br>  type: object<br>  realization: HashMap<br>  additionalProperties:<br>    type: string</pre>         | `private Map<String, String> cache = new HashMap<>();` |
| Supported values | `ArrayList`, `LinkedList`, `HashSet`, `HashMap`, `LinkedHashMap`                                                                 |                                                        |

---

### 9. `format: existing`, `pathForGenerateMessage`, `removeSchema`

| Attribute                | YAML                                                                                                                                       | → Java                                                     |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------|
| `format: existing`       | <pre lang="yaml">user:<br>  type: object<br>  format: existing<br>  name: User<br>  package: com.my.domain</pre>                           | `private User user;` + `import com.my.domain.User;`        |
| `pathForGenerateMessage` | <pre lang="yaml">RequestDto:<br>  payload:<br>    pathForGenerateMessage: 'io.github.events'<br>    $ref: '#/components/schemas/Dto'</pre> | class generated in `.../io/github/events/RequestDto.java`  |
| `removeSchema: true`     | <pre lang="yaml">payload:<br>  $ref: '#/components/schemas/Temp'<br>  removeSchema: true</pre>                                             | schema `Temp` is **not generated**, only fields in message |
---

### 10. Interfaces

| Type         | YAML                                                                                                                                                                                                                                                 | → Java                                                                                                                            |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Marker       | <pre lang="yaml">Marker:<br>  type: object<br>  format: interface</pre>                                                                                                                                                                              | <pre lang="java">public interface Marker {}</pre>                                                                                 |
| With methods | <pre lang="yaml">UserService:<br>  type: object<br>  format: interface<br>  imports:<br>    - com.my.dto.User<br>  methods:<br>    createUser:<br>      description: "Creates a new user"<br>      definition: "User createUser(String email)"</pre> | <pre lang="java">public interface UserService {<br>    /** Creates a new user */<br>    User createUser(String email);<br>}</pre> |
---

## 📦 Integration: YOJO Gradle Plugin (recommended)

```gradle
plugins {
    id 'ru.yojo.codegen.gradle-plugin' version '0.5.0'
}

yojo {
    configurations {
        main {
            specificationProperties {
                api {
                    specName = "asyncapi.yaml"
                    inputDirectory = "./asyncapi/"
                    outputDirectory = "${layout.buildDirectory.get()}/generated/sources/yojo/main/java"
                    packageLocation = "com.mycompany.api"
                }
                // Multiple specs supported:
                // events {
                //     specName = "events.yaml"
                //     inputDirectory = "./asyncapi/events/"
                //     ...
                // }
            }

            // Global defaults
            springBootVersion = "3.2.0"  // selects jakarta/javax
            lombok {
                enable = true
                accessors {
                    enable = true
                    fluent = false
                    chain = true
                }
                equalsAndHashCode {
                    enable = true
                    callSuper = false
                }
                allArgsConstructor = true
                noArgsConstructor = true
            }
        }
    }
}

sourceSets {
    main.java.srcDir("${layout.buildDirectory.get()}/generated/sources/yojo/main/java")
}

tasks.compileJava.dependsOn(generateClasses)
```

🔗 [Official Gradle Plugin](https://plugins.gradle.org/plugin/io.github.yojo-generator.gradle-plugin)  
📦 [GitHub](https://github.com/yojo-generator/gradle-plugin)

---

## 📋 YAML ↔ Java Mapping

### Scalar Types
| YAML                                                      | → Java           | Imports                                                      |
|-----------------------------------------------------------|------------------|--------------------------------------------------------------|
| `type: string`, `format: uuid`                            | `UUID`           | `java.util.UUID`                                             |
| `type: object`, `format: date`                            | `LocalDate`      | `java.time.LocalDate`                                        |
| `type: string`, `format: local-date-time`                 | `LocalDateTime`  | `java.time.LocalDateTime`                                    |
| `type: object`, `format: date-time`                       | `OffsetDateTime` | `java.time.OffsetDateTime`                                   |
| `type: number`, `format: big-decimal`, `multipleOf: 0.01` | `BigDecimal`     | `java.math.BigDecimal`, `@Digits(integer = 1, fraction = 2)` |

### Collections and Maps

| YAML                                                                                                                           | → Java                     |
|--------------------------------------------------------------------------------------------------------------------------------|----------------------------|
| <pre lang="yaml">type: array<br>items:<br>  type: string</pre>                                                                 | `List<String>`             |
| <pre lang="yaml">type: array<br>format: set<br>items:<br>  type: integer</pre>                                                 | `Set<Integer>`             |
| <pre lang="yaml">type: object<br>additionalProperties:<br>  type: string</pre>                                                 | `Map<String, String>`      |
| <pre lang="yaml">type: object<br>format: uuidMap<br>additionalProperties:<br>  $ref: '#/components/schemas/User'</pre>         | `Map<UUID, User>`          |
| <pre lang="yaml">type: object<br>additionalProperties:<br>  type: array<br>  format: set<br>  items:<br>    type: string</pre> | `Map<String, Set<String>>` |
---

## 🧰 Supported Attributes (per `Dictionary.java`)

| Attribute                 | Where                           | Type                                        | Example                                     | Generation                                   |
|---------------------------|---------------------------------|---------------------------------------------|---------------------------------------------|----------------------------------------------|
| `realization`             | `items`, `additionalProperties` | `string`                                    | `realization: ArrayList`                    | `= new ArrayList<>()`                        |
| `primitive`               | field                           | `boolean`                                   | `primitive: true`                           | `int`, `boolean`, `long`                     |
| `multipleOf`              | number                          | `number`                                    | `multipleOf: 0.01`                          | `@Digits(integer = 1, fraction = 2)`         |
| `digits`                  | number                          | `string`                                    | `digits: "integer=2,fraction=2"`            | `@Digits(...)` (**legacy**)                  |
| `validationGroups`        | schema                          | `list`                                      | `validationGroups: [Create.class]`          | `@NotBlank(groups = {Create.class})`         |
| `validationGroupsImports` | schema                          | `list`                                      | `validationGroupsImports: [pkg.Validation]` | `import pkg.Validation;`                     |
| `validateByGroups`        | schema                          | `list`                                      | `validateByGroups: [name, email]`           | annotations only on listed fields            |
| `lombok.*`                | schema/message                  | nested                                      | see Gradle config                           | `@Data`, `@Accessors`, `@AllArgsConstructor` |
| `extends`, `implements`   | schema/message                  | `fromClass`, `fromPackage`, `fromInterface` | see examples                                | `extends X implements Y`                     |
| `format: interface`       | schema                          | —                                           | —                                           | `interface X { ... }`                        |
| `format: existing`        | field                           | `name`, `package`                           | —                                           | `import`, no class gen                       |
| `pathForGenerateMessage`  | `message.payload`               | `string`                                    | `io.github.events`                          | custom message package                       |
| `removeSchema`            | `message.payload`               | `boolean`                                   | `removeSchema: true`                        | skip DTO gen for `$ref` target               |
| `x-enumNames`             | enum                            | `map`                                       | `SUCCESS: "Ok"`                             | enum + description field                     |

---

## 🗺 Roadmap (2025–2026)

| Feature                      | Status                 | Description                                      |
|------------------------------|------------------------|--------------------------------------------------|
| **Jackson annotations**      | ✅ In progress          | `@JsonProperty`, `@JsonFormat`, `@JsonInclude`   |
| **AsyncAPI spec validation** | ✅ In progress          | Validate `$ref`, `type`, `format`, circular refs |
| **Lombok extensions**        | ✅ In progress          | `@Builder`, `@Singular`, `@SuperBuilder`         |
| **OpenAPI 3.1 support**      | 🚧 Planned for Q2 2026 | Only after AsyncAPI 3.0 stabilization            |
| **Freemarker templates**     | 🚧 Planned             | For enterprise customizations                    |

---

## 📬 Contact

- **Author**: Vladimir Morozkin
- **📧 Email**: `jvmorozkin@gmail.com`
- **📟 Telegram**: [`@vmorozkin`](https://t.me/vmorozkin)
- **GitHub**: [yojo-generator](https://github.com/yojo-generator)

> 🙌 Send your contracts — I’ll make a PoC.  
> 🐞 PRs and issues are welcome!

---

## ⚖️ License

Distributed under the **Apache License 2.0**.  
See [LICENSE.md](LICENSE.md).

---

🚀 Let’s generate some code!  
**AsyncAPI → Java — fast, precise, zero manual work.**

---