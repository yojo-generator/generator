package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.GroupRenameChannel;
import slack.common.GroupRenameType;

/**
* A private channel was renamed.
*/
@Generated("Yojo")
public class GroupRename {


    private GroupRenameType type;

    @Valid
    private GroupRenameChannel channel;
    public void setType(GroupRenameType type) {
        this.type = type;
    }
    public GroupRenameType getType() {
        return type;
    }
    public void setChannel(GroupRenameChannel channel) {
        this.channel = channel;
    }
    public GroupRenameChannel getChannel() {
        return channel;
    }
}