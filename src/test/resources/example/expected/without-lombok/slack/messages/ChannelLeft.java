package slack.messages;

import slack.common.ChannelLeftType;
import javax.annotation.processing.Generated;

/**
* You left a channel.
*/
@Generated("Yojo")
public class ChannelLeft {

    private ChannelLeftType type;

    private String channel;

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}