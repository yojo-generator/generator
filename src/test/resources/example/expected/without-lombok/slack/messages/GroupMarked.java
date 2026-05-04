package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupMarkedType;

@Generated("Yojo")
/**
* A private channel read marker was updated.
*/
public class GroupMarked {


    private GroupMarkedType type;

    private String channel;

    private String ts;
    public void setType(GroupMarkedType type) {
        this.type = type;
    }
    public GroupMarkedType getType() {
        return type;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getTs() {
        return ts;
    }
}