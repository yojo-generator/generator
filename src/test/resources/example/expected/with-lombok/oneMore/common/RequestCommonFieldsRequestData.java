package oneMore.common;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import javax.validation.constraints.Max;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;

/**
* Request parameters object
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestCommonFieldsRequestData {

    /**
     * Application source
     */
    @NotBlank
    private String source;

    /**
     * Application number
     * Example: 12365
     */
    @Min(0)
    @Max(99999999)
    private Integer orderNumber;

}