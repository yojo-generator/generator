package example.testGenerate.oneMore.common;

import example.testGenerate.oneMore.common.ApproveV1Context;
import jakarta.validation.constraints.NotNull;
import example.testGenerate.oneMore.common.ApproveV1RequestData;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Approval message schema
*/
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