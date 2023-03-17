![Screenshot](./src/main/resources/css/yojo.png)
### YOJO
#### YAML to POJO generator 


Developed according to 📗 [Official documentation](https://www.asyncapi.com/docs/reference/specification/v2.6.0)

- 👉 [Description](#Description)
- 🎓 [Examples](#Examples)
- 🔥 [Releases](#Releases)
- 💻 [Developers](#Developers)

## Description
The generator works with schema objects described in `components:`

An excerpt from the 📜 AsyncApi documentation:

The AsyncAPI Schema Object is a JSON Schema vocabulary which extends JSON Schema Core and Validation vocabularies. 
As such, any keyword available for those vocabularies is by definition available in AsyncAPI, and will work the exact same way, including but not limited to:
1. [ ] `title`
2. [x] `type`
3. [x] `required`
4. [ ] `multipleOf`
5. [ ] `maximum`
6. [ ] `exclusiveMaximum`
7. [ ] `minimum`
8. [ ] `exclusiveMinimum`
9. [x] `maxLength`
10. [x] `minLength`
11. [ ] `pattern` (This string SHOULD be a valid regular expression, according to the ECMA 262 regular expression dialect)
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
25. [ ] `additionalProperties`
26. [ ] `additionalItems`
27. [x] `items`
28. [ ] `propertyNames`
29. [ ] `contains`
30. [ ] `allOf`
31. [ ] `oneOf`
32. [ ] `anyOf`
33. [ ] `not`

The following properties are taken from the JSON Schema definition but their definitions were adjusted to the AsyncAPI Specification.

1. [x] `description` - CommonMark syntax can be used for rich text representation.
2. [x] `format` - See Data Type Formats for further details. While relying on JSON Schema's defined formats, the AsyncAPI Specification offers a few additional predefined formats.
3. [ ] `default` - The default value represents what would be assumed by the consumer of the input as the value of the schema if one is not provided. Unlike JSON Schema, the value MUST conform to the defined type for the Schema Object defined at the same level. For example, of type is string, then default can be "foo" but cannot be 1.
4. [x] `$ref` - Alternatively, any time a Schema Object can be used, a Reference Object can be used in its place. This allows referencing definitions in place of defining them inline. It is appropriate to clarify that the $ref keyword MUST follow the behavior described by Reference Object instead of the one in JSON Schema definition.

**Not all keywords are currently available.
Available are marked with a checkbox.**

## Examples
#### Let's see the following example of a product store:
```
components:
  messages:
    Request:
      payload:
        $ref: '#/components/schemas/SaveGoodsRequest'
    Response:
      payload:
        $ref: '#/components/schemas/SaveGoodsResponse'
  schemas:
    SaveGoodsRequest:
      type: object
      required:
        - messageId
        - goods
      properties:
        messageId:
          type: string
          minLength: 1
          maxLength: 36
          description: "message id"
          example: "d88646bd-36c8-45b6-866a-17009e88736a"
        goods:
          type: array
          items:
            $ref: '#/components/schemas/Good'
    Good:
      type: object
      required:
        - isNewGood
        - name
      properties:
        isNewGood:
          type: boolean
          example: "FALSE"
          description: "Sign of a new product"
        name:
          type: string
          minLength: 1
          maxLength: 60
          example: "T-shirt"
          description: "Good name"
        manufactureDate:
          type: string
          format: date
          example: "2023-01-02"
          description: "Date of manufacture"
    SaveGoodsResponse:
      type: object
      properties:
        status:
          type: string
          example: "success"
          description: "Item save status"
        data:
          $ref: '#/components/schemas/Data'
        error:
          $ref: '#/components/schemas/Error'
    Data:
      type: string
      description: "Номер товара"
      example: "2143512341234"
    Error:
      type: object
      required:
        - code
        - message
      properties:
        code:
          type: integer
          enum:
            - 0
            - 1
            - 2
            - 3
        message:
          type: string
          enum:
            - Good creation error!
            - Internal error!
            - Incorrect request!
          example: Incorrect request!
```

Based on this example, we are silencing several files with the extension `.java`:
* SaveGoodsRequest.java
* SaveGoodsResponse.java
* Good.java
* Error.java

Examples of the main generated files:
* SaveGoodsRequest.java
```
import javax.validation.constrains.Size
import javax.validation.constrains.NotBlank
import javax.validation.Valid
import java.util.List

public class SaveGoodsRequest {

    /**
     * message Id
     * Example: d88646bd-36c8-45b6-866a-17009e88736a
     */
    @NotBlank
    @Size(min = 1, max = 36)
    private String messageId;

    @Valid
    private List<Good> parameters;

}
```
* SaveGoodsResponse.java
```
import javax.validation.Valid

public class SaveGoodsResponse {

    /**
     * Item save status
     * Example: success
     */
    private String status;

    /**
     * Номер товара
     * Example: 2143512341234
     */
    private String data;

    @Valid
    private Error error;

}
```
See examples [here](./examples)

## Releases
### 💥 Release 0.1.0:
##### Currently implemented the following 📈 features:
* 📈 Support for the following keywords:
  * `type`
  * `required`
  * `maxLength`
  * `minLength`
  * `enum`
  * `examples`
  * `properties`
  * `items`
  * `description`
  * `format`
  * `$ref`
* 📈 Added the following annotations based on keywords:
  * `@NotNull`
  * `@NotEmpty`
  * `@NotBlank`
  * `@Size`
  * `@Pattern`
  * `@Valid`
* 📈 Added required imports according to annotations
* 📈 Filling JavaDoc based on keywords:
  * `description`
  * `example`
  * `enum`
* 📈 Added getters and setters
* 📈 Added Lombok
  * If a lombok is selected, the following annotations will be annotated:
    * @Data 
    * @NoArgsConstructor
  * Added optionally annotations: 
    * @AllArgsConstructor
    * @Accessors(fluent = true, chain = true)

## 💻 Developers
* 😎 Vladimir Morozkin
  * 💬 Contacts:
    * 📧 Email: vmorozkin@it-one.ru
    * 📟 Telegram: @vmorozkin
