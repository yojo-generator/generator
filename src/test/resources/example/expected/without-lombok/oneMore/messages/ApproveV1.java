package example.testGenerate.oneMore.messages;

import example.testGenerate.oneMore.common.ApproveV1Context;
import jakarta.validation.constraints.NotNull;
import example.testGenerate.oneMore.common.ApproveV1RequestData;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
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