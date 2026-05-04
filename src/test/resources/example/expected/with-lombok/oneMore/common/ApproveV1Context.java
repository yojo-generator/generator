package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Integration context data
*/
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