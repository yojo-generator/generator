package example.testGenerate.oneMore.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;

/**
* Request parameters object
*/
@Generated("Yojo")
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

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}