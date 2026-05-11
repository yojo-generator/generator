package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.ChannelJoinedChannel;
import slack.common.ChannelJoinedType;

/**
* You joined a channel.
*/
@Generated("Yojo")
public class ChannelJoined {


    private ChannelJoinedType type;

    @Valid
    private ChannelJoinedChannel channel;
    public void setType(ChannelJoinedType type) {
        this.type = type;
    }
    public ChannelJoinedType getType() {
        return type;
    }
    public void setChannel(ChannelJoinedChannel channel) {
        this.channel = channel;
    }
    public ChannelJoinedChannel getChannel() {
        return channel;
    }
}