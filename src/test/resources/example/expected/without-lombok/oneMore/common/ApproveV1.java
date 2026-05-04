package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import oneMore.common.ApproveV1Context;
import oneMore.common.ApproveV1RequestData;

@Generated("Yojo")
/**
* Approval message schema
*/
public class ApproveV1 {


    /**
     * Integration context data
     */
    @Valid
    @NotNull
    private ApproveV1Context context;

    /**
     * Request parameters object
     */
    @Valid
    @NotNull
    private ApproveV1RequestData requestData;
    public void setContext(ApproveV1Context context) {
        this.context = context;
    }
    public ApproveV1Context getContext() {
        return context;
    }
    public void setRequestData(ApproveV1RequestData requestData) {
        this.requestData = requestData;
    }
    public ApproveV1RequestData getRequestData() {
        return requestData;
    }
}