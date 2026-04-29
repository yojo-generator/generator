package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupUnarchiveType;
import javax.annotation.processing.Generated;

/**
* A private channel was unarchived.
*/
@Generated("Yojo")
public class GroupUnarchive {

    private GroupUnarchiveType type;

    private String channel;

    private String user;

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
}