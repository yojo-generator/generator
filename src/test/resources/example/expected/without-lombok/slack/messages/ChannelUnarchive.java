package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelUnarchiveType;

/**
* A channel was unarchived.
*/
@Generated("Yojo")
public class ChannelUnarchive {


    private ChannelUnarchiveType type;

    private String channel;

    private String user;
    public void setType(ChannelUnarchiveType type) {
        this.type = type;
    }
    public ChannelUnarchiveType getType() {
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