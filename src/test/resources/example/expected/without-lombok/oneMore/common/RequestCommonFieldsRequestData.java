package oneMore.common;

import javax.validation.constraints.NotBlank;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
* Request parameters object
*/
@Generated("Yojo")
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

    public void setSource(String source) {
        this.source = source;
    }
    public String getSource() {
        return source;
    }
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }
}