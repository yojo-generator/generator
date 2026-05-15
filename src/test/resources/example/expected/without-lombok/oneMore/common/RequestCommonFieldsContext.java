package oneMore.common;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* Context
*/
@Generated("Yojo")
public class RequestCommonFieldsContext {


    /**
     * Method name
     */
    @NotBlank
    private String methodName;

    /**
     * Business key
     * Example: c4b74ef4-be12-49c1-992b-a98be82b7125
     */
    @NotNull
    private UUID businessKey;

    /**
     * Instance ID
     * Example: c4b74ef4-be12-49c1-992b-a98be82b7125
     */
    private UUID instanceId;

    /**
     * Request ID
     * Example: c4b74ef4-be12-49c1-992b-a98be82b7125
     */
    @NotNull
    private UUID requestId;
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setBusinessKey(UUID businessKey) {
        this.businessKey = businessKey;
    }
    public UUID getBusinessKey() {
        return businessKey;
    }
    public void setInstanceId(UUID instanceId) {
        this.instanceId = instanceId;
    }
    public UUID getInstanceId() {
        return instanceId;
    }
    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
    public UUID getRequestId() {
        return requestId;
    }
    @Override
    public String toString() {
        return "RequestCommonFieldsContext{" +
                "methodName=" + methodName + ", " +
                "businessKey=" + businessKey + ", " +
                "instanceId=" + instanceId + ", " +
                "requestId=" + requestId +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestCommonFieldsContext that = (RequestCommonFieldsContext) o;
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(businessKey, that.businessKey) &&
                Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, businessKey, instanceId, requestId);
    }
}