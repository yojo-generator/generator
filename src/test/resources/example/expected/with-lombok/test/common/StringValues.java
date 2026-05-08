package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Here was located all supported strings values
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class StringValues {


    private String stringWithDefaultValue = "5";

    private String stringWithDefaultValueWithoutBrackets = "5";

    private String stringValueWithoutRequired;

    @NotBlank
    private String stringValueWithRequired;

    @NotBlank
    @Size(min = 1)
    private String stringValueNotBlankSizeMinLength;

    @NotBlank
    @Size(max = 2)
    private String stringValueNotBlankSizeMaxLength;

    @Size(min = 1, max = 2)
    private String stringValueSizeMinMaxLength;

    @Pattern(regexp = "^d{6}$")
    private String stringWithPattern;

    @NotBlank
    @Size(min = 1, max = 2)
    @Pattern(regexp = "^d{6}$")
    private String stringWithAllAnnotations;
}