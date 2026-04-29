package asyncapi.common;

import java.time.OffsetDateTime;
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
}