package oneMore.common;

import javax.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import oneMore.common.ClientAddress;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

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