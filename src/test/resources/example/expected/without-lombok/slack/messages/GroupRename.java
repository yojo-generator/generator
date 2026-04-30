package slack.messages;

import slack.common.GroupRenameChannel;
import javax.validation.Valid;
import slack.common.GroupRenameType;
import javax.annotation.processing.Generated;

/**
* A private channel was renamed.
*/
@Generated("Yojo")
public class GroupRename {

    private GroupRenameType type;

    @Valid
    private GroupRenameChannel channel;

    public void setChannel(GroupRenameChannel channel) {
        this.channel = channel;
    }
    public GroupRenameChannel getChannel() {
        return channel;
    }
}