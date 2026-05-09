package example.testGenerate.test.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* Here was located all supported numeric values
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}