package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupJoinedType;
import example.testGenerate.slack.common.GroupJoinedChannel;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* You joined a private channel.
*/
@Generated("Yojo")
public class GroupJoined {

    private GroupJoinedType type;

    @Valid
    private GroupJoinedChannel channel;

    public void setChannel(GroupJoinedChannel channel) {
        this.channel = channel;
    }
    public GroupJoinedChannel getChannel() {
        return channel;
    }
}