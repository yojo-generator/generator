package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelJoinedChannel;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.ChannelJoinedType;

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