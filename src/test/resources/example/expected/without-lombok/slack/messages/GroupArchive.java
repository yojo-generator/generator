package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupArchiveType;

/**
* A private channel was archived.
*/
@Generated("Yojo")
public class GroupArchive {


    private GroupArchiveType type;

    private String channel;
    public void setType(GroupArchiveType type) {
        this.type = type;
    }
    public GroupArchiveType getType() {
        return type;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}