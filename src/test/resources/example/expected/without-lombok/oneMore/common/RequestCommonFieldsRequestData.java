package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import javax.annotation.processing.Generated;

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