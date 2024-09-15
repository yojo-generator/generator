![Screenshot](./yojo.png)
### YOJO
#### YAML to POJO generator 


Developed according to 📗 [Official documentation](https://www.asyncapi.com/docs/reference/specification/v2.6.0)

<!-- TOC -->
    * [YOJO](#yojo)
      * [YAML to POJO generator](#yaml-to-pojo-generator-)
  * 👉 [Description](#description)
  * 🎓 [Examples](#examples)
  * 🎓 [How to fill AsyncApi contract](#how-to-fill-asyncapi-contract)
    * [Message Object](#message-object)
    * [Schema Object](#schema-object)
    * [Аttributes](#аttributes)
    * [Custom YAML attributes](#custom-yaml-attributes)
  * 💻 [Developers](#developers)
<!-- TOC -->

## Description
The generator works with schema objects described in `components:`

An excerpt from the 📜 AsyncApi documentation:

The AsyncAPI Schema Object is a JSON Schema vocabulary which extends JSON Schema Core and Validation vocabularies. 
As such, any keyword available for those vocabularies is by definition available in AsyncAPI, and will work the exact same way, including but not limited to:
1. [x] `title`
2. [x] `type`
3. [x] `required`
4. [ ] `multipleOf`
5. [x] `maximum`
6. [ ] `exclusiveMaximum`
7. [x] `minimum`
8. [ ] `exclusiveMinimum`
9. [x] `maxLength`
10. [x] `minLength`
11. [x] `pattern` (This string SHOULD be a valid regular expression, according to the ECMA 262 regular expression dialect)
12. [ ] `maxItems`
13. [ ] `minItems`
14. [ ] `uniqueItems`
15. [ ] `maxProperties`
16. [ ] `minProperties`
17. [x] `enum`
18. [ ] `const`
19. [x] `examples`
20. [ ] `if` / `then` / `else`
21. [ ] `readOnly`
22. [ ] `writeOnly`
23. [x] `properties`
24. [ ] `patternProperties`
25. [x] `additionalProperties`
26. [ ] `additionalItems`
27. [x] `items`
28. [ ] `propertyNames`
29. [ ] `contains`
30. [x] `allOf`
31. [x] `oneOf`
32. [x] `anyOf`
33. [ ] `not`

The following properties are taken from the JSON Schema definition but their definitions were adjusted to the AsyncAPI Specification.

1. [x] `description` - CommonMark syntax can be used for rich text representation.
2. [x] `format` - See Data Type Formats for further details. While relying on JSON Schema's defined formats, the AsyncAPI Specification offers a few additional predefined formats.
3. [x] `default` - The default value represents what would be assumed by the consumer of the input as the value of the schema if one is not provided. Unlike JSON Schema, the value MUST conform to the defined type for the Schema Object defined at the same level. For example, of type is string, then default can be "foo" but cannot be 1.
4. [x] `$ref` - Alternatively, any time a Schema Object can be used, a Reference Object can be used in its place. This allows referencing definitions in place of defining them inline. It is appropriate to clarify that the $ref keyword MUST follow the behavior described by Reference Object instead of the one in JSON Schema definition.

**Not all keywords are currently available.
Available are marked with a checkbox.**

## Examples

Find examples [here](./examples) for more cases

## How to fill AsyncApi contract

At the moment, yojo is actively being refined and supplemented with various custom attributes for convenient code generation in Java. 
The features available today are:

Please **note** that it is always **necessary** to fill in the `type` attribute!

### Message Object

You can fill message like this:

    RequestDtoByRef:
      payload:
        #This attribute will help you generate the message in the package you need.
        pathForGenerateMessage: 'io.github.somepath'
        $ref: '#/components/schemas/RequestDtoSchema'

And you will get message like:

    package example.testGenerate.contract.test.io.github.somepath;
    
    public class RequestDtoByRef {
    
    }

Also you can make generate messages with propeties like:

    RequestDtoByRefAndProperties:
      payload:
        $ref: '#/components/schemas/RequestDtoSchema'
        properties:
          someString:
            type: String

And you will getting this one:
    
    package example.testGenerate.contract.test.messages;

    public class RequestDtoByRefAndProperties {
        private String someString;
    }

This is what the option looks like with all possible attributes:

    RequestDtoWithDoubleInheritance:
      payload:
        #You can use enable true/false on lombok
        lombok:
          accessors:
            chain: true
            fluent: false
          equalsAndHashCode:
            enable: true
            callSuper: true
          allArgsConstructor: true
          noArgsConstructor: false
        #removeSchema - schema in the reference will not be deleted.
        removeSchema: false
        $ref: '#/components/schemas/RequestDtoSchema'
        implements:
          fromInterface:
            - testGenerate.InterfaceForImpl
            - testGenerate.InterfaceForImpl2
        extends:
          fromClass: ClassForExtends
          fromPackage: example.testGenerate.contract.test.common

In the end, you will easily obtain such a class:

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public class RequestDtoWithDoubleInheritance extends ClassForExtends implements InterfaceForImpl,InterfaceForImpl2 {
    }

**See more in** [here](./examples)

### Schema Object

This is what the option looks like with all possible attributes:

    RequestDtoSchema:
      type: object
      lombok:
        accessors:
          enable: false
      implements:
        fromInterface:
          - testGenerate.InterfaceForImpl
          - testGenerate.InterfaceForImpl2
      extends:
        fromClass: ClassForExtends
        fromPackage: example.testGenerate.contract.test.common

In the end, you will easily obtain such a class:

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RequestDtoSchema extends ClassForExtends implements InterfaceForImpl,InterfaceForImpl2 {
    }


### Аttributes
| Common <br/>name | type    | format         | Result         |
|------------------|---------|----------------|----------------|
| integer          | integer | int32          | Integer        |
| long             | integer | int64          | Long           |
| float            | number  | float          | Float          |
| double           | number  | double         | Double         |
| byte             | string  | byte           | Byte           |
| bigInteger       | number  | bigInteger     | BigInteger     |
| bigDecimal       | number  | bigDecimal     | BigDecimal     |
| boolean          | boolean | -              | Boolean        |
| string           | string  | -              | String         |
| uuid             | string  | uuid           | UUID           |
| date             | object  | simple-date    | Date           |
| date             | string  | date           | LocalDate      |
| dateTime         | string  | date-time      | LocalDateTime  |
| offsetDateTime   | string  | offsetDateTime | OffsetDateTime |

The list is constantly being updated; for more details, see the examples.

### Custom YAML attributes

| Attribute                                                              | Description                                                     | Example                                                           |
|------------------------------------------------------------------------|-----------------------------------------------------------------|-------------------------------------------------------------------|
| realization                                                            | Use this attribute to specify the type of collection being used | Accepts realization by name. = new ArrayList/HashSet/HashMap etc. |
| primitive                                                              | Use for generating primitive types.                             | Accepts a boolean. long, int, byte, boolean etc.                  |
| existing<br/>package                                                   | Use for generating with using existing object.                  |                                                                   |
| digits                                                                 | Use for generating @Digits annotation                           |                                                                   |
| removeSchema                                                           | Used in messages. See examples                                  |                                                                   |
| interface<br/>definition                                               | Use for generating interfaces.                                  |                                                                   |
| extends<br/>implements<br/>fromClass<br/>fromPackage<br/>fromInterface | Attributes for extending or implementing objects                |                                                                   |

The list is constantly being updated; for more details, see the examples.

Also the generator is capable of generating collections(Map,List,Set) and enums.


Please contact me if you need assistance; I will definitely help you. 
This instruction only covers a small part of what can be generated. 
Use the examples, or download the open source code and run the test cases, commenting out the file deletion in them beforehand.

## Developers
* 😎 Vladimir Morozkin
  * 💬 Contacts:
    * 📧 Email: `jvmorozkin@gmail.com`
    * 📟 Telegram: `@vmorozkin`
