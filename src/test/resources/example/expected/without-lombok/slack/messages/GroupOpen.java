package slack.messages;

import slack.common.GroupOpenType;
import javax.annotation.processing.Generated;

/**
* You opened a private channel.
*/
@Generated("Yojo")
public class GroupOpen {

    private GroupOpenType type;

    private String user;

    private String channel;

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