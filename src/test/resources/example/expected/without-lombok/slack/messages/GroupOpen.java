package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupOpenType;

/**
* You opened a private channel.
*/
@Generated("Yojo")
public class GroupOpen {


    private GroupOpenType type;

    private String user;

    private String channel;
    public void setType(GroupOpenType type) {
        this.type = type;
    }
    public GroupOpenType getType() {
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