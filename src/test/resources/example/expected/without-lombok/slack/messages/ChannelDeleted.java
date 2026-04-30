package slack.messages;

import slack.common.ChannelDeletedType;
import javax.annotation.processing.Generated;

/**
* A channel was deleted.
*/
@Generated("Yojo")
public class ChannelDeleted {

    private ChannelDeletedType type;

    private String channel;

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}