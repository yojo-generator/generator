package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;

@Generated("Yojo")
/**
* Integration context data
*/
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