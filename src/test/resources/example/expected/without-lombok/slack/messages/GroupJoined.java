package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.GroupJoinedChannel;
import slack.common.GroupJoinedType;

/**
* You joined a private channel.
*/
@Generated("Yojo")
public class GroupJoined {


    private GroupJoinedType type;

    @Valid
    private GroupJoinedChannel channel;
    public void setType(GroupJoinedType type) {
        this.type = type;
    }
    public GroupJoinedType getType() {
        return type;
    }
    public void setChannel(GroupJoinedChannel channel) {
        this.channel = channel;
    }
    public GroupJoinedChannel getChannel() {
        return channel;
    }
}