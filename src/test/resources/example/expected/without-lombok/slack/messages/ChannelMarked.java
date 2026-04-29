package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelMarkedType;
import javax.annotation.processing.Generated;

/**
* Your channel read marker was updated.
*/
@Generated("Yojo")
public class ChannelMarked {

    private ChannelMarkedType type;

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