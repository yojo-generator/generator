package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupRenameChannel;
import example.testGenerate.slack.common.GroupRenameType;
import jakarta.validation.Valid;
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