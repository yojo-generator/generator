package oneMore.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
    @Override
    public String toString() {
        return "RequestCommonFieldsRequestData{" +
                "source=" + source + ", " +
                "orderNumber=" + orderNumber +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestCommonFieldsRequestData that = (RequestCommonFieldsRequestData) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(orderNumber, that.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, orderNumber);
    }
}