package oneMore.common;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Request parameters object
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ApproveV1RequestData {


    /**
     * Application status
     * Example: APPROVED
     */
    @NotBlank
    private String status;

    /**
     * Comment
     * Example: Application approved
     */
    private String comment;

    /**
     * Record update timestamp
     * Example: 2025-04-02T17:32:28Z
     */
    @NotNull
    private OffsetDateTime updatedAt;
}