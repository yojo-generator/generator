package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupCloseType;
import javax.annotation.processing.Generated;

/**
* You closed a private channel.
*/
@Generated("Yojo")
public class GroupClose {

    private GroupCloseType type;

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