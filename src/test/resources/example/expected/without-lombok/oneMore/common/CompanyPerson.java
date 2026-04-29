package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import example.testGenerate.oneMore.common.ClientAddress;
import jakarta.validation.constraints.Max;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import javax.annotation.processing.Generated;

@Generated("Yojo")
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

    public void setActualAddress(ClientAddress actualAddress) {
        this.actualAddress = actualAddress;
    }
    public ClientAddress getActualAddress() {
        return actualAddress;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getSex() {
        return sex;
    }
}