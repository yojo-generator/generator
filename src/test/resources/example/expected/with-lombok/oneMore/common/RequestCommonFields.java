package example.testGenerate.oneMore.common;

import example.testGenerate.oneMore.common.RequestCommonFieldsContext;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.Valid;
import example.testGenerate.oneMore.common.RequestCommonFieldsRequestData;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
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