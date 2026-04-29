package slack.messages;

import slack.common.ChannelRenameType;
import slack.common.ChannelRenameChannel;
import javax.validation.Valid;
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