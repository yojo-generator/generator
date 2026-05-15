package oneMore.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import oneMore.common.ApproveV1Context;
import oneMore.common.ApproveV1RequestData;

/**
* Approval message schema
*/
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
    @Override
    public String toString() {
        return "ApproveV1{" +
                "context=" + context + ", " +
                "requestData=" + requestData +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApproveV1 that = (ApproveV1) o;
        return Objects.equals(context, that.context) &&
                Objects.equals(requestData, that.requestData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, requestData);
    }
}