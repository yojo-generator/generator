package asyncapi.common;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;

@Generated("Yojo")
public class LightMeasuredPayload {


    /**
     * Light intensity measured in lumens.
     */
    @Min(0)
    private Integer lumens;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;
    public void setLumens(Integer lumens) {
        this.lumens = lumens;
    }
    public Integer getLumens() {
        return lumens;
    }
    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public OffsetDateTime getSentAt() {
        return sentAt;
    }
    @Override
    public String toString() {
        return "LightMeasuredPayload{" +
                "lumens=" + lumens + ", " +
                "sentAt=" + sentAt +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightMeasuredPayload that = (LightMeasuredPayload) o;
        return Objects.equals(lumens, that.lumens) &&
                Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lumens, sentAt);
    }
}