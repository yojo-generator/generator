package asyncapi.messages;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
* Command a particular streetlight to dim the lights.
*/
@Generated("Yojo")
public class DimLight {

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
}