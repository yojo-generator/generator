package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelCreatedType;
import example.testGenerate.slack.common.ChannelCreatedChannel;
import jakarta.validation.Valid;
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