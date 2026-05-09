package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelArchiveType;

/**
* A channel was archived.
*/
@Generated("Yojo")
public class ChannelArchive {


    private ChannelArchiveType type;

    private String channel;

    private String user;
    public void setType(ChannelArchiveType type) {
        this.type = type;
    }
    public ChannelArchiveType getType() {
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