package oneMore.common;

import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Context
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}