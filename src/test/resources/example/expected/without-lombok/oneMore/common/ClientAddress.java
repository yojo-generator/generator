package oneMore.common;

import javax.validation.constraints.Size;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Pattern;

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