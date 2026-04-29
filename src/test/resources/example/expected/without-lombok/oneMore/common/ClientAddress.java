package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import javax.annotation.processing.Generated;

/**
* Address
*/
@Generated("Yojo")
public class ClientAddress {

    /**
     * City
     * Example: Moscow
     */
    @Size(min = 0, max = 255)
    @Pattern(regexp = "$(pattern-Unicode)")
    private String city;

    /**
     * Country
     * Example: RU
     */
    @Size(min = 0, max = 31)
    @Pattern(regexp = "$(pattern-Unicode)")
    private String country;

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }
}