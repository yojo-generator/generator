package oneMore.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import oneMore.common.ClientAddress;

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
    @Override
    public String toString() {
        return "CompanyPerson{" +
                "actualAddress=" + actualAddress + ", " +
                "name=" + name + ", " +
                "sex=" + sex +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPerson that = (CompanyPerson) o;
        return Objects.equals(actualAddress, that.actualAddress) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualAddress, name, sex);
    }
}