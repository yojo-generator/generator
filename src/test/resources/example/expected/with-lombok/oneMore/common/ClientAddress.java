package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

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