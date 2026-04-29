package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImOpenType;
import javax.annotation.processing.Generated;

/**
* You opened a DM.
*/
@Generated("Yojo")
public class ImOpen {

    private ImOpenType type;

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