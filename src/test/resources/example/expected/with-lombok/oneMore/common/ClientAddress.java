package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* Address
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}