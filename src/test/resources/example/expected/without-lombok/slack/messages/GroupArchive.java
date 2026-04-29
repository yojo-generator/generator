package slack.messages;

import slack.common.GroupArchiveType;
import javax.annotation.processing.Generated;

/**
* A private channel was archived.
*/
@Generated("Yojo")
public class GroupArchive {

    private GroupArchiveType type;

    private String channel;

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}