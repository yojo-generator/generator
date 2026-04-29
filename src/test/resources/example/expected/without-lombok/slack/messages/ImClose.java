package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImCloseType;
import javax.annotation.processing.Generated;

/**
* You closed a DM.
*/
@Generated("Yojo")
public class ImClose {

    private ImCloseType type;

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