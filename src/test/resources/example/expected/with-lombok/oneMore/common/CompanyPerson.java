package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import example.testGenerate.oneMore.common.ClientAddress;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CompanyPerson {

    /**
     * Physical address
     */
    @Valid
    @NotNull
    private ClientAddress actualAddress;

    /**
     * Taxpayer Identification Number (TIN)
     * Example: 772388193796
     */
    @NotBlank
    @Size(min = 0, max = 12)
    @Pattern(regexp = "$(pattern-Unicode)")
    private String name;

    /**
     * Gender
     * Example: 1
     */
    @Min(1)
    @Max(2147483647)
    private Integer sex;

}