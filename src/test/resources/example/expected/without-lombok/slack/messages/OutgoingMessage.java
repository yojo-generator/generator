package slack.messages;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import slack.common.OutgoingMessageType;

/**
* A message was sent to a channel.
*/
@Generated("Yojo")
public class OutgoingMessage {

    private BigDecimal id;

    private OutgoingMessageType type;

    private String channel;

    private String text;

    public void setId(BigDecimal id) {
        this.id = id;
    }
    public BigDecimal getId() {
        return id;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
}