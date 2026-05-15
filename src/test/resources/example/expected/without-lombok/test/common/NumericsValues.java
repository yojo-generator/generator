package example.testGenerate.test.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
* Here was located all supported numeric values
*/
@Generated("Yojo")
public class NumericsValues {


    private Byte byteValue;

    private Integer integerValue;

    private Integer integerValueWithFormat;

    private Long longValue;

    private Double doubleValue;

    private Float floatValue;

    @Digits(integer = 4, fraction = 2)
    private BigDecimal bigDecimalValue;

    private BigInteger bigIntegerValue;

    @Min(2)
    @Max(4)
    @NotNull
    @Digits(integer = 1, fraction = 0)
    private Byte byteValueWithAnnotations;

    @NotNull
    @Max(44)
    @Min(22)
    @Digits(integer = 2, fraction = 0)
    private Integer integerValueWithAnnotations;

    @NotNull
    @Max(44)
    @Min(22)
    @Digits(integer = 2, fraction = 0)
    private Long longValueWithAnnotations;

    @NotNull
    private Double doubleValueWithAnnotations;

    @NotNull
    private Float floatValueWithAnnotations;

    @NotNull
    @Max(44)
    @Min(22)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal bigDecimalValueWithAnnotations;

    @NotNull
    @Max(44)
    @Min(22)
    @Digits(integer = 2, fraction = 2)
    private BigInteger bigIntegerValueWithAnnotations;
    public void setByteValue(Byte byteValue) {
        this.byteValue = byteValue;
    }
    public Byte getByteValue() {
        return byteValue;
    }
    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }
    public Integer getIntegerValue() {
        return integerValue;
    }
    public void setIntegerValueWithFormat(Integer integerValueWithFormat) {
        this.integerValueWithFormat = integerValueWithFormat;
    }
    public Integer getIntegerValueWithFormat() {
        return integerValueWithFormat;
    }
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }
    public Long getLongValue() {
        return longValue;
    }
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    public Double getDoubleValue() {
        return doubleValue;
    }
    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }
    public Float getFloatValue() {
        return floatValue;
    }
    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }
    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }
    public void setBigIntegerValue(BigInteger bigIntegerValue) {
        this.bigIntegerValue = bigIntegerValue;
    }
    public BigInteger getBigIntegerValue() {
        return bigIntegerValue;
    }
    public void setByteValueWithAnnotations(Byte byteValueWithAnnotations) {
        this.byteValueWithAnnotations = byteValueWithAnnotations;
    }
    public Byte getByteValueWithAnnotations() {
        return byteValueWithAnnotations;
    }
    public void setIntegerValueWithAnnotations(Integer integerValueWithAnnotations) {
        this.integerValueWithAnnotations = integerValueWithAnnotations;
    }
    public Integer getIntegerValueWithAnnotations() {
        return integerValueWithAnnotations;
    }
    public void setLongValueWithAnnotations(Long longValueWithAnnotations) {
        this.longValueWithAnnotations = longValueWithAnnotations;
    }
    public Long getLongValueWithAnnotations() {
        return longValueWithAnnotations;
    }
    public void setDoubleValueWithAnnotations(Double doubleValueWithAnnotations) {
        this.doubleValueWithAnnotations = doubleValueWithAnnotations;
    }
    public Double getDoubleValueWithAnnotations() {
        return doubleValueWithAnnotations;
    }
    public void setFloatValueWithAnnotations(Float floatValueWithAnnotations) {
        this.floatValueWithAnnotations = floatValueWithAnnotations;
    }
    public Float getFloatValueWithAnnotations() {
        return floatValueWithAnnotations;
    }
    public void setBigDecimalValueWithAnnotations(BigDecimal bigDecimalValueWithAnnotations) {
        this.bigDecimalValueWithAnnotations = bigDecimalValueWithAnnotations;
    }
    public BigDecimal getBigDecimalValueWithAnnotations() {
        return bigDecimalValueWithAnnotations;
    }
    public void setBigIntegerValueWithAnnotations(BigInteger bigIntegerValueWithAnnotations) {
        this.bigIntegerValueWithAnnotations = bigIntegerValueWithAnnotations;
    }
    public BigInteger getBigIntegerValueWithAnnotations() {
        return bigIntegerValueWithAnnotations;
    }
    @Override
    public String toString() {
        return "NumericsValues{" +
                "byteValue=" + byteValue + ", " +
                "integerValue=" + integerValue + ", " +
                "integerValueWithFormat=" + integerValueWithFormat + ", " +
                "longValue=" + longValue + ", " +
                "doubleValue=" + doubleValue + ", " +
                "floatValue=" + floatValue + ", " +
                "bigDecimalValue=" + bigDecimalValue + ", " +
                "bigIntegerValue=" + bigIntegerValue + ", " +
                "byteValueWithAnnotations=" + byteValueWithAnnotations + ", " +
                "integerValueWithAnnotations=" + integerValueWithAnnotations + ", " +
                "longValueWithAnnotations=" + longValueWithAnnotations + ", " +
                "doubleValueWithAnnotations=" + doubleValueWithAnnotations + ", " +
                "floatValueWithAnnotations=" + floatValueWithAnnotations + ", " +
                "bigDecimalValueWithAnnotations=" + bigDecimalValueWithAnnotations + ", " +
                "bigIntegerValueWithAnnotations=" + bigIntegerValueWithAnnotations +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericsValues that = (NumericsValues) o;
        return Objects.equals(byteValue, that.byteValue) &&
                Objects.equals(integerValue, that.integerValue) &&
                Objects.equals(integerValueWithFormat, that.integerValueWithFormat) &&
                Objects.equals(longValue, that.longValue) &&
                Objects.equals(doubleValue, that.doubleValue) &&
                Objects.equals(floatValue, that.floatValue) &&
                Objects.equals(bigDecimalValue, that.bigDecimalValue) &&
                Objects.equals(bigIntegerValue, that.bigIntegerValue) &&
                Objects.equals(byteValueWithAnnotations, that.byteValueWithAnnotations) &&
                Objects.equals(integerValueWithAnnotations, that.integerValueWithAnnotations) &&
                Objects.equals(longValueWithAnnotations, that.longValueWithAnnotations) &&
                Objects.equals(doubleValueWithAnnotations, that.doubleValueWithAnnotations) &&
                Objects.equals(floatValueWithAnnotations, that.floatValueWithAnnotations) &&
                Objects.equals(bigDecimalValueWithAnnotations, that.bigDecimalValueWithAnnotations) &&
                Objects.equals(bigIntegerValueWithAnnotations, that.bigIntegerValueWithAnnotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(byteValue, integerValue, integerValueWithFormat, longValue, doubleValue, floatValue, bigDecimalValue, bigIntegerValue, byteValueWithAnnotations, integerValueWithAnnotations, longValueWithAnnotations, doubleValueWithAnnotations, floatValueWithAnnotations, bigDecimalValueWithAnnotations, bigIntegerValueWithAnnotations);
    }
}