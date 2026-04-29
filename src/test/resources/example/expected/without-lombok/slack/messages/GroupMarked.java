package example.testGenerate.slack.messages;

import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.GroupMarkedType;

/**
* A private channel read marker was updated.
*/
@Generated("Yojo")
public class GroupMarked {

    private GroupMarkedType type;

    private String channel;

    private String ts;

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