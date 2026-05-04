package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.ChannelCreatedChannel;
import slack.common.ChannelCreatedType;

@Generated("Yojo")
/**
* A channel was created.
*/
public class ChannelCreated {


    private ChannelCreatedType type;

    @Valid
    private ChannelCreatedChannel channel;
    public void setType(ChannelCreatedType type) {
        this.type = type;
    }
    public ChannelCreatedType getType() {
        return type;
    }
    public void setChannel(ChannelCreatedChannel channel) {
        this.channel = channel;
    }
    public ChannelCreatedChannel getChannel() {
        return channel;
    }
}