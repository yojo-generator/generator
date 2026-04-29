package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import javax.annotation.processing.Generated;

/**
* Integration context data
*/
@Generated("Yojo")
public class ApproveV1Context {

    /**
     * Method name
     */
    @NotBlank
    private String methodName = "verificationApprove";

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getMethodName() {
        return methodName;
    }
}