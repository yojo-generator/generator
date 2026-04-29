package example.testGenerate.test.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import javax.annotation.processing.Generated;

/**
* Here was located all supported strings values
*/
@Generated("Yojo")
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

    public void setStringWithDefaultValue(String stringWithDefaultValue) {
        this.stringWithDefaultValue = stringWithDefaultValue;
    }
    public String getStringWithDefaultValue() {
        return stringWithDefaultValue;
    }
    public void setStringWithDefaultValueWithoutBrackets(String stringWithDefaultValueWithoutBrackets) {
        this.stringWithDefaultValueWithoutBrackets = stringWithDefaultValueWithoutBrackets;
    }
    public String getStringWithDefaultValueWithoutBrackets() {
        return stringWithDefaultValueWithoutBrackets;
    }
    public void setStringValueWithoutRequired(String stringValueWithoutRequired) {
        this.stringValueWithoutRequired = stringValueWithoutRequired;
    }
    public String getStringValueWithoutRequired() {
        return stringValueWithoutRequired;
    }
    public void setStringValueWithRequired(String stringValueWithRequired) {
        this.stringValueWithRequired = stringValueWithRequired;
    }
    public String getStringValueWithRequired() {
        return stringValueWithRequired;
    }
    public void setStringValueNotBlankSizeMinLength(String stringValueNotBlankSizeMinLength) {
        this.stringValueNotBlankSizeMinLength = stringValueNotBlankSizeMinLength;
    }
    public String getStringValueNotBlankSizeMinLength() {
        return stringValueNotBlankSizeMinLength;
    }
    public void setStringValueNotBlankSizeMaxLength(String stringValueNotBlankSizeMaxLength) {
        this.stringValueNotBlankSizeMaxLength = stringValueNotBlankSizeMaxLength;
    }
    public String getStringValueNotBlankSizeMaxLength() {
        return stringValueNotBlankSizeMaxLength;
    }
    public void setStringValueSizeMinMaxLength(String stringValueSizeMinMaxLength) {
        this.stringValueSizeMinMaxLength = stringValueSizeMinMaxLength;
    }
    public String getStringValueSizeMinMaxLength() {
        return stringValueSizeMinMaxLength;
    }
    public void setStringWithPattern(String stringWithPattern) {
        this.stringWithPattern = stringWithPattern;
    }
    public String getStringWithPattern() {
        return stringWithPattern;
    }
    public void setStringWithAllAnnotations(String stringWithAllAnnotations) {
        this.stringWithAllAnnotations = stringWithAllAnnotations;
    }
    public String getStringWithAllAnnotations() {
        return stringWithAllAnnotations;
    }
}