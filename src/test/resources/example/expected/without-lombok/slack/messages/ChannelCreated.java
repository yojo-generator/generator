package slack.messages;

import javax.validation.Valid;
import slack.common.ChannelCreatedType;
import slack.common.ChannelCreatedChannel;
import javax.annotation.processing.Generated;

/**
* A channel was created.
*/
@Generated("Yojo")
public class ChannelCreated {

    private ChannelCreatedType type;

    @Valid
    private ChannelCreatedChannel channel;

    public void setChannel(ChannelCreatedChannel channel) {
        this.channel = channel;
    }
    public ChannelCreatedChannel getChannel() {
        return channel;
    }
}