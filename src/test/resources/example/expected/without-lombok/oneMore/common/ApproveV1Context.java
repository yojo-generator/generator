package oneMore.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;

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
    @Override
    public String toString() {
        return "ApproveV1Context{" +
                "methodName=" + methodName +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApproveV1Context that = (ApproveV1Context) o;
        return Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName);
    }
}