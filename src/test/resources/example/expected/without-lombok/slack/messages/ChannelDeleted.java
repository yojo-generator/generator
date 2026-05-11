package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelDeletedType;

/**
* A channel was deleted.
*/
@Generated("Yojo")
public class ChannelDeleted {


    private ChannelDeletedType type;

    private String channel;
    public void setType(ChannelDeletedType type) {
        this.type = type;
    }
    public ChannelDeletedType getType() {
        return type;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}