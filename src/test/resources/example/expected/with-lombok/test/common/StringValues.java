package example.testGenerate.test.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import javax.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

/**
* Here was located all supported strings values
*/
@Generated("Yojo")
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