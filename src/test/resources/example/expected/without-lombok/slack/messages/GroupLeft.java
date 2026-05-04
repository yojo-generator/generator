package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupLeftType;

@Generated("Yojo")
/**
* You left a private channel.
*/
public class GroupLeft {


    private GroupLeftType type;

    private String channel;
    public void setType(GroupLeftType type) {
        this.type = type;
    }
    public GroupLeftType getType() {
        return type;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}