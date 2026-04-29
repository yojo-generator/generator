package example.testGenerate.asyncapi.common;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import example.testGenerate.asyncapi.common.TurnOnOffPayloadCommand;

@Generated("Yojo")
public class TurnOnOffPayload {

    /**
     * Whether to turn on or off the light.
     */
    private TurnOnOffPayloadCommand command;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;

    public void setCommand(TurnOnOffPayloadCommand command) {
        this.command = command;
    }
    public TurnOnOffPayloadCommand getCommand() {
        return command;
    }
    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public OffsetDateTime getSentAt() {
        return sentAt;
    }
}