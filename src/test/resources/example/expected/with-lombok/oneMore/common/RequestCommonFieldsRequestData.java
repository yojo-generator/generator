package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Request parameters object
*/
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