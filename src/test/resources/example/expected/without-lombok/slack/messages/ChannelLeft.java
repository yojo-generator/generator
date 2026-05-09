package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelLeftType;

/**
* You left a channel.
*/
@Generated("Yojo")
public class ChannelLeft {


    private ChannelLeftType type;

    private String channel;
    public void setType(ChannelLeftType type) {
        this.type = type;
    }
    public ChannelLeftType getType() {
        return type;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}