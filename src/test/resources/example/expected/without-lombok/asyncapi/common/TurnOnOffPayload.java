package asyncapi.common;

import asyncapi.common.TurnOnOffPayloadCommand;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.processing.Generated;

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
    @Override
    public String toString() {
        return "TurnOnOffPayload{" +
                "command=" + command + ", " +
                "sentAt=" + sentAt +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnOnOffPayload that = (TurnOnOffPayload) o;
        return Objects.equals(command, that.command) &&
                Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, sentAt);
    }
}