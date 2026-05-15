package oneMore.common;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Override
    public String toString() {
        return "ApproveV1RequestData{" +
                "status=" + status + ", " +
                "comment=" + comment + ", " +
                "updatedAt=" + updatedAt +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApproveV1RequestData that = (ApproveV1RequestData) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, comment, updatedAt);
    }
}