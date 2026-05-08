package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.ChannelRenameChannel;
import slack.common.ChannelRenameType;

@Generated("Yojo")
/**
* A channel was renamed.
*/
public class ChannelRename {


    private ChannelRenameType type;

    @Valid
    private ChannelRenameChannel channel;
    public void setType(ChannelRenameType type) {
        this.type = type;
    }
    public ChannelRenameType getType() {
        return type;
    }
    public void setChannel(ChannelRenameChannel channel) {
        this.channel = channel;
    }
    public ChannelRenameChannel getChannel() {
        return channel;
    }
}