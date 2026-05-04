package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import oneMore.common.RequestCommonFieldsContext;
import oneMore.common.RequestCommonFieldsRequestData;

@Generated("Yojo")
/**
* Common meta-tags
*/
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