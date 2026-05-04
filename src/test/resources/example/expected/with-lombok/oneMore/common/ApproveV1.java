package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import oneMore.common.ApproveV1Context;
import oneMore.common.ApproveV1RequestData;

@Generated("Yojo")
/**
* Approval message schema
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}