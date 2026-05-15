package asyncapi.common;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Generated("Yojo")
public class DimLightPayload {


    /**
     * Percentage to which the light should be dimmed to.
     */
    @Min(0)
    @Max(100)
    private Integer percentage;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
    public Integer getPercentage() {
        return percentage;
    }
    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public OffsetDateTime getSentAt() {
        return sentAt;
    }
    @Override
    public String toString() {
        return "DimLightPayload{" +
                "percentage=" + percentage + ", " +
                "sentAt=" + sentAt +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DimLightPayload that = (DimLightPayload) o;
        return Objects.equals(percentage, that.percentage) &&
                Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(percentage, sentAt);
    }
}