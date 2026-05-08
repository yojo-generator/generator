package slack.messages;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import slack.common.OutgoingMessageType;

@Generated("Yojo")
/**
* A message was sent to a channel.
*/
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
    public void setType(OutgoingMessageType type) {
        this.type = type;
    }
    public OutgoingMessageType getType() {
        return type;
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