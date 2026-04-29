package oneMore.common;

import oneMore.common.RequestCommonFieldsContext;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import oneMore.common.RequestCommonFieldsRequestData;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Common meta-tags
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestCommonFields {

    /**
     * Context
     */
    @Valid
    @NotNull
    private RequestCommonFieldsContext context;

    /**
     * Request parameters object
     */
    @Valid
    @NotNull
    private RequestCommonFieldsRequestData requestData;

}