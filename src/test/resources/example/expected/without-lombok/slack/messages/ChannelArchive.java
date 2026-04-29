package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelArchiveType;
import javax.annotation.processing.Generated;

/**
* A channel was archived.
*/
@Generated("Yojo")
public class ChannelArchive {

    private ChannelArchiveType type;

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