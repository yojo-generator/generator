package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ImCloseType;

@Generated("Yojo")
/**
* You closed a DM.
*/
public class ImClose {


    private ImCloseType type;

    private String channel;

    private String user;
    public void setType(ImCloseType type) {
        this.type = type;
    }
    public ImCloseType getType() {
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