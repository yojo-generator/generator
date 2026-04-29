package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Integration context data
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ApproveV1Context {

    /**
     * Method name
     */
    @NotBlank
    private String methodName = "verificationApprove";

}