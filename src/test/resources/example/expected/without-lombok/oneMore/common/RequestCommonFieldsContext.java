package oneMore.common;

import javax.validation.constraints.NotBlank;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.annotation.processing.Generated;

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
}