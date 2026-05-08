package oneMore.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import oneMore.common.RequestCommonFieldsContext;
import oneMore.common.RequestCommonFieldsRequestData;

@Generated("Yojo")
/**
* Common meta-tags
*/
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