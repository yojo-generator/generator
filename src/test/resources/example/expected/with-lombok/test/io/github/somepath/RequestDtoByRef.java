package example.testGenerate.test.io.github.somepath;

import example.testGenerate.test.common.ClassForExtends;
import example.testGenerate.test.common.CollectionTypes;
import example.testGenerate.test.common.EnumResultWithDescription;
import example.testGenerate.test.common.EnumResultWithoutDescription;
import example.testGenerate.test.common.NumericsValues;
import example.testGenerate.test.common.ObjectTypes;
import example.testGenerate.test.common.PolymorphExampleThree;
import example.testGenerate.test.common.PolymorphPolymorphExampleOnePolymorphExampleTwo;
import example.testGenerate.test.common.RequestDtoSchemaInnerEnumWithDescription;
import example.testGenerate.test.common.RequestDtoSchemaInnerEnumWithoutDescription;
import example.testGenerate.test.common.RequestDtoSchemaInnerEnumWithoutDescriptionSmall;
import example.testGenerate.test.common.RequestDtoSchemaInnerSchema;
import example.testGenerate.test.common.SomeObject;
import example.testGenerate.test.common.StringValues;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import testGenerate.ExistingClass;
import testGenerate.InterfaceForImpl2;
import testGenerate.InterfaceForImpl;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestDtoByRef extends ClassForExtends implements InterfaceForImpl,InterfaceForImpl2 {


    @Valid
    private PolymorphPolymorphExampleOnePolymorphExampleTwo polymorph;

    @Valid
    private PolymorphExampleThree polymorphWithInsidePolymorph;

    /**
     * Here was located all supported object types
     */
    @Valid
    private ObjectTypes objectTypes;

    /**
     * Here was located all supported strings values
     */
    @Valid
    @NotNull
    private StringValues StringValues;

    /**
     * Here was located all supported numeric values
     */
    @Valid
    private NumericsValues numericsValues;

    /**
     * Here was located all supported collections
     */
    @Valid
    @NotEmpty
    private List<CollectionTypes> collectionTypes;

    @NotNull
    private Integer integerValidationField;

    @NotEmpty
    private List<UUID> uuidValidationList;

    @Valid
    private RequestDtoSchemaInnerSchema innerSchema;

    /**
     * Map generation Example
     */
    private Map<String, Integer> mapStringInteger;

    /**
     * Map generation Example
     */
    private Map<String, Object> mapStringObject;

    /**
     * Map generation Example
     */
    private Map<String, Object> mapStringObjectWithHashMap = new HashMap<>();

    /**
     * Map generation Example
     */
    private Map<String, Object> mapStringObjectWithLinkedHashMap = new LinkedHashMap<>();

    /**
     * Map generation Example
     */
    private Map<String, SomeObject> mapStringCustomObject;

    /**
     * Map generation Example
     */
    private Map<String, ExistingClass> mapStringExistingObject;

    /**
     * Map generation Example
     */
    private Map<String, Set<SomeObject>> mapStringSetOfCustomObject;

    /**
     * Map generation Example
     */
    private Map<String, Set<String>> mapStringSetOfStrings;

    /**
     * Map generation Example
     */
    private Map<String, Set<ExistingClass>> mapStringSetOfExistingObject;

    /**
     * Map generation Example
     */
    private Map<UUID, Integer> mapUUIDInteger;

    /**
     * Map generation Example
     */
    private Map<UUID, Object> mapUUIDObject;

    /**
     * Map generation Example
     */
    private Map<UUID, Object> mapUUIDObjectWithHashMap = new HashMap<>();

    /**
     * Map generation Example
     */
    private Map<UUID, Object> mapUUIDObjectWithLinkedHashMap = new LinkedHashMap<>();

    /**
     * Map generation Example
     */
    private Map<UUID, SomeObject> mapUUIDCustomObject;

    /**
     * Map generation Example
     */
    private Map<UUID, ExistingClass> mapUUIDExistingObject;

    /**
     * Map generation Example
     */
    private Map<UUID, Set<SomeObject>> mapUUIDSetOfCustomObject;

    /**
     * Map generation Example
     */
    private Map<UUID, Set<String>> mapUUIDSetOfStrings;

    /**
     * Map generation Example
     */
    private Map<UUID, Set<ExistingClass>> mapUUIDSetOfExistingObject;

    /**
     * Result of request
     * <p>
     * Supported values:
     * «SUCCESS» - Ok
     * «DECLINE» - Decline
     * «ERROR» - Error

     */
    private RequestDtoSchemaInnerEnumWithDescription innerEnumWithDescription;

    /**
     * Result of request
     * <p>
     * Supported values:
     * «SUCCESS» - Ok
     * «DECLINE» - Decline
     * «ERROR» - Error

     */
    private RequestDtoSchemaInnerEnumWithoutDescription innerEnumWithoutDescription;

    /**
     * Result of request
     * <p>
     * Supported values:
     * «success» - Ok
     * «decline» - Decline
     * «error» - Error

     */
    private RequestDtoSchemaInnerEnumWithoutDescriptionSmall innerEnumWithoutDescriptionSmall;

    private EnumResultWithDescription enumResultWithDescription;

    private EnumResultWithoutDescription enumResultWithoutDescription;

    @Valid
    private SomeObject someObject;
}