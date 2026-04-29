package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelRenameChannel;
import example.testGenerate.slack.common.ChannelRenameType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* A channel was renamed.
*/
@Generated("Yojo")
public class ChannelRename {

    private ChannelRenameType type;

    @Valid
    private ChannelRenameChannel channel;

    public void setChannel(ChannelRenameChannel channel) {
        this.channel = channel;
    }
    public ChannelRenameChannel getChannel() {
        return channel;
    }
}