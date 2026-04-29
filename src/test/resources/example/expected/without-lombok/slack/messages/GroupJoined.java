package slack.messages;

import slack.common.GroupJoinedChannel;
import slack.common.GroupJoinedType;
import javax.validation.Valid;
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