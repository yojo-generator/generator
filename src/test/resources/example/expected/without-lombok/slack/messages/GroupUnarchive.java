package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupUnarchiveType;

@Generated("Yojo")
/**
* A private channel was unarchived.
*/
public class GroupUnarchive {


    private GroupUnarchiveType type;

    private String channel;

    private String user;
    public void setType(GroupUnarchiveType type) {
        this.type = type;
    }
    public GroupUnarchiveType getType() {
        return type;
    }
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