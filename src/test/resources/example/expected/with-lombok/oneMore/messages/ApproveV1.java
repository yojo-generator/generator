package oneMore.messages;

import oneMore.common.ApproveV1Context;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import oneMore.common.ApproveV1RequestData;

@Generated("Yojo")
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