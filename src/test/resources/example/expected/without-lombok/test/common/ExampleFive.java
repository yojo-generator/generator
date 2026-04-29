package example.testGenerate.test.common;

import example.testGenerate.test.common.PolymorphExampleThree;
import example.testGenerate.test.common.SomeObject;
import java.util.List;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithoutDescriptionSmall;
import example.testGenerate.test.common.EnumResultWithDescription;
import example.testGenerate.test.common.NumericsValues;
import example.testGenerate.test.common.StringValues;
import java.util.HashMap;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithoutDescription;
import java.util.Map;
import example.testGenerate.test.common.EnumResultWithoutDescription;
import java.util.UUID;
import example.testGenerate.test.common.ExampleFiveInnerEnumWithDescription;
import java.util.Set;
import example.testGenerate.test.common.CollectionTypes;
import example.testGenerate.test.common.ExampleFiveInnerSchema;
import testGenerate.ExistingClass;
import java.util.LinkedHashMap;
import example.testGenerate.test.common.PolymorphPolymorphExampleOnePolymorphExampleTwo;
import example.testGenerate.test.common.ObjectTypes;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
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

    public void setOneMoreField(Boolean oneMoreField) {
        this.oneMoreField = oneMoreField;
    }
    public Boolean getOneMoreField() {
        return oneMoreField;
    }
    public void setFromFive(String fromFive) {
        this.fromFive = fromFive;
    }
    public String getFromFive() {
        return fromFive;
    }
    public void setPolymorph(PolymorphPolymorphExampleOnePolymorphExampleTwo polymorph) {
        this.polymorph = polymorph;
    }
    public PolymorphPolymorphExampleOnePolymorphExampleTwo getPolymorph() {
        return polymorph;
    }
    public void setPolymorphWithInsidePolymorph(PolymorphExampleThree polymorphWithInsidePolymorph) {
        this.polymorphWithInsidePolymorph = polymorphWithInsidePolymorph;
    }
    public PolymorphExampleThree getPolymorphWithInsidePolymorph() {
        return polymorphWithInsidePolymorph;
    }
    public void setObjectTypes(ObjectTypes objectTypes) {
        this.objectTypes = objectTypes;
    }
    public ObjectTypes getObjectTypes() {
        return objectTypes;
    }
    public void setStringValues(StringValues stringValues) {
        this.stringValues = stringValues;
    }
    public StringValues getStringValues() {
        return stringValues;
    }
    public void setNumericsValues(NumericsValues numericsValues) {
        this.numericsValues = numericsValues;
    }
    public NumericsValues getNumericsValues() {
        return numericsValues;
    }
    public void setCollectionTypes(List<CollectionTypes> collectionTypes) {
        this.collectionTypes = collectionTypes;
    }
    public List<CollectionTypes> getCollectionTypes() {
        return collectionTypes;
    }
    public void setIntegerValidationField(Integer integerValidationField) {
        this.integerValidationField = integerValidationField;
    }
    public Integer getIntegerValidationField() {
        return integerValidationField;
    }
    public void setUuidValidationList(List<UUID> uuidValidationList) {
        this.uuidValidationList = uuidValidationList;
    }
    public List<UUID> getUuidValidationList() {
        return uuidValidationList;
    }
    public void setInnerSchema(ExampleFiveInnerSchema innerSchema) {
        this.innerSchema = innerSchema;
    }
    public ExampleFiveInnerSchema getInnerSchema() {
        return innerSchema;
    }
    public void setMapStringInteger(Map<String, Integer> mapStringInteger) {
        this.mapStringInteger = mapStringInteger;
    }
    public Map<String, Integer> getMapStringInteger() {
        return mapStringInteger;
    }
    public void setMapStringObject(Map<String, Object> mapStringObject) {
        this.mapStringObject = mapStringObject;
    }
    public Map<String, Object> getMapStringObject() {
        return mapStringObject;
    }
    public void setMapStringObjectWithHashMap(Map<String, Object> mapStringObjectWithHashMap) {
        this.mapStringObjectWithHashMap = mapStringObjectWithHashMap;
    }
    public Map<String, Object> getMapStringObjectWithHashMap() {
        return mapStringObjectWithHashMap;
    }
    public void setMapStringObjectWithLinkedHashMap(Map<String, Object> mapStringObjectWithLinkedHashMap) {
        this.mapStringObjectWithLinkedHashMap = mapStringObjectWithLinkedHashMap;
    }
    public Map<String, Object> getMapStringObjectWithLinkedHashMap() {
        return mapStringObjectWithLinkedHashMap;
    }
    public void setMapStringCustomObject(Map<String, SomeObject> mapStringCustomObject) {
        this.mapStringCustomObject = mapStringCustomObject;
    }
    public Map<String, SomeObject> getMapStringCustomObject() {
        return mapStringCustomObject;
    }
    public void setMapStringExistingObject(Map<String, ExistingClass> mapStringExistingObject) {
        this.mapStringExistingObject = mapStringExistingObject;
    }
    public Map<String, ExistingClass> getMapStringExistingObject() {
        return mapStringExistingObject;
    }
    public void setMapStringSetOfCustomObject(Map<String, Set<SomeObject>> mapStringSetOfCustomObject) {
        this.mapStringSetOfCustomObject = mapStringSetOfCustomObject;
    }
    public Map<String, Set<SomeObject>> getMapStringSetOfCustomObject() {
        return mapStringSetOfCustomObject;
    }
    public void setMapStringSetOfStrings(Map<String, Set<String>> mapStringSetOfStrings) {
        this.mapStringSetOfStrings = mapStringSetOfStrings;
    }
    public Map<String, Set<String>> getMapStringSetOfStrings() {
        return mapStringSetOfStrings;
    }
    public void setMapStringSetOfExistingObject(Map<String, Set<ExistingClass>> mapStringSetOfExistingObject) {
        this.mapStringSetOfExistingObject = mapStringSetOfExistingObject;
    }
    public Map<String, Set<ExistingClass>> getMapStringSetOfExistingObject() {
        return mapStringSetOfExistingObject;
    }
    public void setMapUUIDInteger(Map<UUID, Integer> mapUUIDInteger) {
        this.mapUUIDInteger = mapUUIDInteger;
    }
    public Map<UUID, Integer> getMapUUIDInteger() {
        return mapUUIDInteger;
    }
    public void setMapUUIDObject(Map<UUID, Object> mapUUIDObject) {
        this.mapUUIDObject = mapUUIDObject;
    }
    public Map<UUID, Object> getMapUUIDObject() {
        return mapUUIDObject;
    }
    public void setMapUUIDObjectWithHashMap(Map<UUID, Object> mapUUIDObjectWithHashMap) {
        this.mapUUIDObjectWithHashMap = mapUUIDObjectWithHashMap;
    }
    public Map<UUID, Object> getMapUUIDObjectWithHashMap() {
        return mapUUIDObjectWithHashMap;
    }
    public void setMapUUIDObjectWithLinkedHashMap(Map<UUID, Object> mapUUIDObjectWithLinkedHashMap) {
        this.mapUUIDObjectWithLinkedHashMap = mapUUIDObjectWithLinkedHashMap;
    }
    public Map<UUID, Object> getMapUUIDObjectWithLinkedHashMap() {
        return mapUUIDObjectWithLinkedHashMap;
    }
    public void setMapUUIDCustomObject(Map<UUID, SomeObject> mapUUIDCustomObject) {
        this.mapUUIDCustomObject = mapUUIDCustomObject;
    }
    public Map<UUID, SomeObject> getMapUUIDCustomObject() {
        return mapUUIDCustomObject;
    }
    public void setMapUUIDExistingObject(Map<UUID, ExistingClass> mapUUIDExistingObject) {
        this.mapUUIDExistingObject = mapUUIDExistingObject;
    }
    public Map<UUID, ExistingClass> getMapUUIDExistingObject() {
        return mapUUIDExistingObject;
    }
    public void setMapUUIDSetOfCustomObject(Map<UUID, Set<SomeObject>> mapUUIDSetOfCustomObject) {
        this.mapUUIDSetOfCustomObject = mapUUIDSetOfCustomObject;
    }
    public Map<UUID, Set<SomeObject>> getMapUUIDSetOfCustomObject() {
        return mapUUIDSetOfCustomObject;
    }
    public void setMapUUIDSetOfStrings(Map<UUID, Set<String>> mapUUIDSetOfStrings) {
        this.mapUUIDSetOfStrings = mapUUIDSetOfStrings;
    }
    public Map<UUID, Set<String>> getMapUUIDSetOfStrings() {
        return mapUUIDSetOfStrings;
    }
    public void setMapUUIDSetOfExistingObject(Map<UUID, Set<ExistingClass>> mapUUIDSetOfExistingObject) {
        this.mapUUIDSetOfExistingObject = mapUUIDSetOfExistingObject;
    }
    public Map<UUID, Set<ExistingClass>> getMapUUIDSetOfExistingObject() {
        return mapUUIDSetOfExistingObject;
    }
    public void setInnerEnumWithDescription(ExampleFiveInnerEnumWithDescription innerEnumWithDescription) {
        this.innerEnumWithDescription = innerEnumWithDescription;
    }
    public ExampleFiveInnerEnumWithDescription getInnerEnumWithDescription() {
        return innerEnumWithDescription;
    }
    public void setInnerEnumWithoutDescription(ExampleFiveInnerEnumWithoutDescription innerEnumWithoutDescription) {
        this.innerEnumWithoutDescription = innerEnumWithoutDescription;
    }
    public ExampleFiveInnerEnumWithoutDescription getInnerEnumWithoutDescription() {
        return innerEnumWithoutDescription;
    }
    public void setInnerEnumWithoutDescriptionSmall(ExampleFiveInnerEnumWithoutDescriptionSmall innerEnumWithoutDescriptionSmall) {
        this.innerEnumWithoutDescriptionSmall = innerEnumWithoutDescriptionSmall;
    }
    public ExampleFiveInnerEnumWithoutDescriptionSmall getInnerEnumWithoutDescriptionSmall() {
        return innerEnumWithoutDescriptionSmall;
    }
    public void setEnumResultWithDescription(EnumResultWithDescription enumResultWithDescription) {
        this.enumResultWithDescription = enumResultWithDescription;
    }
    public EnumResultWithDescription getEnumResultWithDescription() {
        return enumResultWithDescription;
    }
    public void setEnumResultWithoutDescription(EnumResultWithoutDescription enumResultWithoutDescription) {
        this.enumResultWithoutDescription = enumResultWithoutDescription;
    }
    public EnumResultWithoutDescription getEnumResultWithoutDescription() {
        return enumResultWithoutDescription;
    }
    public void setSomeObject(SomeObject someObject) {
        this.someObject = someObject;
    }
    public SomeObject getSomeObject() {
        return someObject;
    }
}