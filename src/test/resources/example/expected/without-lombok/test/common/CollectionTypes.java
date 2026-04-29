package example.testGenerate.test.common;

import java.util.List;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import example.testGenerate.test.common.CollectionTypesListWithInnerSchemaAndExistingObject;
import testGenerate.ClassForExtending;
import example.testGenerate.test.common.CollectionTypesInnerSchema;
import java.math.BigInteger;
import java.util.Set;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.annotation.processing.Generated;
import java.net.URI;
import java.math.BigDecimal;

/**
* Here was located all supported collections
*/
@Generated("Yojo")
public class CollectionTypes {

    private List<LocalDate> listOfDate;

    /**
     * This is standalone uri
     */
    private List<URI> listOfStandAloneUri;

    /**
     * This is standalone date
     */
    private List<LocalDate> listOfStandAloneDate;

    private List<LocalDateTime> listOfLocalDateTime;

    private List<OffsetDateTime> listOfOffsetDateTime;

    private List<Integer> listOfInteger;

    private List<Byte> listOfByte;

    private List<Double> listOfDouble;

    private List<Float> listOfFloat;

    private List<BigInteger> listOfBigInteger;

    private List<BigDecimal> listOfBigDecimal;

    private List<Long> listOfLongsWithRealization = new ArrayList<>();

    @Valid
    private List<CollectionTypesListWithInnerSchemaAndExistingObject> listWithInnerSchemaAndExistingObject;

    @Valid
    private List<ClassForExtending> listOfExistingObject;

    @Valid
    private Set<ClassForExtending> setOfExistingObject;

    @Valid
    private List<CollectionTypesInnerSchema> innerSchema;

    private Set<LocalDate> setOfDate;

    private Set<LocalDateTime> setOfLocalDateTime;

    private Set<OffsetDateTime> setOfOffsetDateTime;

    private Set<Integer> setOfInteger;

    private Set<Byte> setOfByte;

    private Set<Double> setOfDouble;

    private Set<Float> setOfFloat;

    private Set<BigInteger> setOfBigInteger;

    private Set<BigDecimal> setOfBigDecimal;

    private Set<Long> setOfLongsWithRealization = new HashSet<>();

    public void setListOfDate(List<LocalDate> listOfDate) {
        this.listOfDate = listOfDate;
    }
    public List<LocalDate> getListOfDate() {
        return listOfDate;
    }
    public void setListOfStandAloneUri(List<URI> listOfStandAloneUri) {
        this.listOfStandAloneUri = listOfStandAloneUri;
    }
    public List<URI> getListOfStandAloneUri() {
        return listOfStandAloneUri;
    }
    public void setListOfStandAloneDate(List<LocalDate> listOfStandAloneDate) {
        this.listOfStandAloneDate = listOfStandAloneDate;
    }
    public List<LocalDate> getListOfStandAloneDate() {
        return listOfStandAloneDate;
    }
    public void setListOfLocalDateTime(List<LocalDateTime> listOfLocalDateTime) {
        this.listOfLocalDateTime = listOfLocalDateTime;
    }
    public List<LocalDateTime> getListOfLocalDateTime() {
        return listOfLocalDateTime;
    }
    public void setListOfOffsetDateTime(List<OffsetDateTime> listOfOffsetDateTime) {
        this.listOfOffsetDateTime = listOfOffsetDateTime;
    }
    public List<OffsetDateTime> getListOfOffsetDateTime() {
        return listOfOffsetDateTime;
    }
    public void setListOfInteger(List<Integer> listOfInteger) {
        this.listOfInteger = listOfInteger;
    }
    public List<Integer> getListOfInteger() {
        return listOfInteger;
    }
    public void setListOfByte(List<Byte> listOfByte) {
        this.listOfByte = listOfByte;
    }
    public List<Byte> getListOfByte() {
        return listOfByte;
    }
    public void setListOfDouble(List<Double> listOfDouble) {
        this.listOfDouble = listOfDouble;
    }
    public List<Double> getListOfDouble() {
        return listOfDouble;
    }
    public void setListOfFloat(List<Float> listOfFloat) {
        this.listOfFloat = listOfFloat;
    }
    public List<Float> getListOfFloat() {
        return listOfFloat;
    }
    public void setListOfBigInteger(List<BigInteger> listOfBigInteger) {
        this.listOfBigInteger = listOfBigInteger;
    }
    public List<BigInteger> getListOfBigInteger() {
        return listOfBigInteger;
    }
    public void setListOfBigDecimal(List<BigDecimal> listOfBigDecimal) {
        this.listOfBigDecimal = listOfBigDecimal;
    }
    public List<BigDecimal> getListOfBigDecimal() {
        return listOfBigDecimal;
    }
    public void setListOfLongsWithRealization(List<Long> listOfLongsWithRealization) {
        this.listOfLongsWithRealization = listOfLongsWithRealization;
    }
    public List<Long> getListOfLongsWithRealization() {
        return listOfLongsWithRealization;
    }
    public void setListWithInnerSchemaAndExistingObject(List<CollectionTypesListWithInnerSchemaAndExistingObject> listWithInnerSchemaAndExistingObject) {
        this.listWithInnerSchemaAndExistingObject = listWithInnerSchemaAndExistingObject;
    }
    public List<CollectionTypesListWithInnerSchemaAndExistingObject> getListWithInnerSchemaAndExistingObject() {
        return listWithInnerSchemaAndExistingObject;
    }
    public void setListOfExistingObject(List<ClassForExtending> listOfExistingObject) {
        this.listOfExistingObject = listOfExistingObject;
    }
    public List<ClassForExtending> getListOfExistingObject() {
        return listOfExistingObject;
    }
    public void setSetOfExistingObject(Set<ClassForExtending> setOfExistingObject) {
        this.setOfExistingObject = setOfExistingObject;
    }
    public Set<ClassForExtending> getSetOfExistingObject() {
        return setOfExistingObject;
    }
    public void setInnerSchema(List<CollectionTypesInnerSchema> innerSchema) {
        this.innerSchema = innerSchema;
    }
    public List<CollectionTypesInnerSchema> getInnerSchema() {
        return innerSchema;
    }
    public void setSetOfDate(Set<LocalDate> setOfDate) {
        this.setOfDate = setOfDate;
    }
    public Set<LocalDate> getSetOfDate() {
        return setOfDate;
    }
    public void setSetOfLocalDateTime(Set<LocalDateTime> setOfLocalDateTime) {
        this.setOfLocalDateTime = setOfLocalDateTime;
    }
    public Set<LocalDateTime> getSetOfLocalDateTime() {
        return setOfLocalDateTime;
    }
    public void setSetOfOffsetDateTime(Set<OffsetDateTime> setOfOffsetDateTime) {
        this.setOfOffsetDateTime = setOfOffsetDateTime;
    }
    public Set<OffsetDateTime> getSetOfOffsetDateTime() {
        return setOfOffsetDateTime;
    }
    public void setSetOfInteger(Set<Integer> setOfInteger) {
        this.setOfInteger = setOfInteger;
    }
    public Set<Integer> getSetOfInteger() {
        return setOfInteger;
    }
    public void setSetOfByte(Set<Byte> setOfByte) {
        this.setOfByte = setOfByte;
    }
    public Set<Byte> getSetOfByte() {
        return setOfByte;
    }
    public void setSetOfDouble(Set<Double> setOfDouble) {
        this.setOfDouble = setOfDouble;
    }
    public Set<Double> getSetOfDouble() {
        return setOfDouble;
    }
    public void setSetOfFloat(Set<Float> setOfFloat) {
        this.setOfFloat = setOfFloat;
    }
    public Set<Float> getSetOfFloat() {
        return setOfFloat;
    }
    public void setSetOfBigInteger(Set<BigInteger> setOfBigInteger) {
        this.setOfBigInteger = setOfBigInteger;
    }
    public Set<BigInteger> getSetOfBigInteger() {
        return setOfBigInteger;
    }
    public void setSetOfBigDecimal(Set<BigDecimal> setOfBigDecimal) {
        this.setOfBigDecimal = setOfBigDecimal;
    }
    public Set<BigDecimal> getSetOfBigDecimal() {
        return setOfBigDecimal;
    }
    public void setSetOfLongsWithRealization(Set<Long> setOfLongsWithRealization) {
        this.setOfLongsWithRealization = setOfLongsWithRealization;
    }
    public Set<Long> getSetOfLongsWithRealization() {
        return setOfLongsWithRealization;
    }
}