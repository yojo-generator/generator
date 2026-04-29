package example.testGenerate.asyncapi.messages;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import example.testGenerate.asyncapi.common.TurnOnOffPayloadCommand;

/**
* Command a particular streetlight to turn the lights on or off.
*/
@Generated("Yojo")
public class TurnOnOff {

    /**
     * Whether to turn on or off the light.
     */
    private TurnOnOffPayloadCommand command;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public OffsetDateTime getSentAt() {
        return sentAt;
    }
}