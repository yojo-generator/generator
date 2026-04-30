package oneMore.common;

import oneMore.common.RequestCommonFieldsContext;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import javax.annotation.processing.Generated;
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
}