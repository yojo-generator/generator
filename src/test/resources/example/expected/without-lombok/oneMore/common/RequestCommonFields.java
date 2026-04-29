package example.testGenerate.oneMore.common;

import example.testGenerate.oneMore.common.RequestCommonFieldsContext;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import example.testGenerate.oneMore.common.RequestCommonFieldsRequestData;
import javax.annotation.processing.Generated;

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