package oneMore.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import oneMore.common.RequestCommonFieldsContext;
import oneMore.common.RequestCommonFieldsRequestData;

/**
* Common meta-tags
*/
@Generated("Yojo")
public class RequestCommonFields {


    /**
     * Context
     */
    @Valid
    @NotNull
    private RequestCommonFieldsContext context;

    /**
     * Request parameters object
     */
    @Valid
    @NotNull
    private RequestCommonFieldsRequestData requestData;
    public void setContext(RequestCommonFieldsContext context) {
        this.context = context;
    }
    public RequestCommonFieldsContext getContext() {
        return context;
    }
    public void setRequestData(RequestCommonFieldsRequestData requestData) {
        this.requestData = requestData;
    }
    public RequestCommonFieldsRequestData getRequestData() {
        return requestData;
    }
    @Override
    public String toString() {
        return "RequestCommonFields{" +
                "context=" + context + ", " +
                "requestData=" + requestData +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestCommonFields that = (RequestCommonFields) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(requestData, that.requestData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, requestData);
    }
}