package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupCloseType;

@Generated("Yojo")
/**
* You closed a private channel.
*/
public class GroupClose {


    private GroupCloseType type;

    private String user;

    private String channel;
    public void setType(GroupCloseType type) {
        this.type = type;
    }
    public GroupCloseType getType() {
        return type;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}