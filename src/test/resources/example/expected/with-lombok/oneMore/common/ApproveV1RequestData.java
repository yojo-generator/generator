package oneMore.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Request parameters object
*/
@Generated("Yojo")
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