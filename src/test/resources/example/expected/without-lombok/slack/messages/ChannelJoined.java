package slack.messages;

import slack.common.ChannelJoinedType;
import slack.common.ChannelJoinedChannel;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

/**
* You joined a channel.
*/
@Generated("Yojo")
public class ChannelJoined {

    private ChannelJoinedType type;

    @Valid
    private ChannelJoinedChannel channel;

    public void setChannel(ChannelJoinedChannel channel) {
        this.channel = channel;
    }
    public ChannelJoinedChannel getChannel() {
        return channel;
    }
}