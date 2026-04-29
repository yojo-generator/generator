package oneMore.common;

import javax.validation.constraints.NotBlank;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Context
*/
@Generated("Yojo")
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