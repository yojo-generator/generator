package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ImOpenType;

@Generated("Yojo")
/**
* You opened a DM.
*/
public class ImOpen {


    private ImOpenType type;

    private String channel;

    private String user;
    public void setType(ImOpenType type) {
        this.type = type;
    }
    public ImOpenType getType() {
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