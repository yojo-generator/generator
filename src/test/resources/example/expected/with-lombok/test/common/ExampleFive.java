package example.testGenerate.test.common;

import example.testGenerate.test.common.PolymorphExampleThree;
import java.util.List;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithoutDescriptionSmall;
import lombok.AllArgsConstructor;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithoutDescription;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.util.UUID;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import example.testGenerate.test.common.PolymorphPolymorphExampleOnePolymorphExampleTwo;
import javax.annotation.processing.Generated;
import example.testGenerate.test.common.SomeObject;
import example.testGenerate.test.common.EnumResultWithDescription;
import example.testGenerate.test.common.NumericsValues;
import example.testGenerate.test.common.StringValues;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import example.testGenerate.test.common.EnumResultWithoutDescription;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithDescription;
import java.util.Set;
import example.testGenerate.test.common.CollectionTypes;
import example.testGenerate.test.common.ExampleFiveInnerSchema;
import testGenerate.ExistingClass;
import example.testGenerate.test.common.ObjectTypes;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ExampleFive {

    private Boolean oneMoreField;

    private String fromFive;

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
    private StringValues stringValues;

    /**
     * Here was located all supported numeric values
     */
    @Valid
    private NumericsValues numericsValues;

    /**
     * Here was located all supported collections
     */
    @Valid
    private List<CollectionTypes> collectionTypes;

    private Integer integerValidationField;

    private List<UUID> uuidValidationList;

    @Valid
    private ExampleFiveInnerSchema innerSchema;

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
    private ExampleFiveInnerEnumWithDescription innerEnumWithDescription;

    /**
     * Result of request
     * <p>
     * Supported values:
     * «SUCCESS» - Ok
     * «DECLINE» - Decline
     * «ERROR» - Error

     */
    private ExampleFiveInnerEnumWithoutDescription innerEnumWithoutDescription;

    /**
     * Result of request
     * <p>
     * Supported values:
     * «success» - Ok
     * «decline» - Decline
     * «error» - Error

     */
    private ExampleFiveInnerEnumWithoutDescriptionSmall innerEnumWithoutDescriptionSmall;

    private EnumResultWithDescription enumResultWithDescription;

    private EnumResultWithoutDescription enumResultWithoutDescription;

    @Valid
    private SomeObject someObject;

}