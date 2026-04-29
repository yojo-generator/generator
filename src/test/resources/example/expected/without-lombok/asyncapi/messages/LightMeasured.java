package example.testGenerate.asyncapi.messages;

import java.time.OffsetDateTime;
import jakarta.validation.constraints.Min;
import javax.annotation.processing.Generated;

/**
* Inform about environmental lighting conditions of a particular streetlight.
*/
@Generated("Yojo")
public class LightMeasured {

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