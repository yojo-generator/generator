package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelMarkedType;

@Generated("Yojo")
/**
* Your channel read marker was updated.
*/
public class ChannelMarked {


    private ChannelMarkedType type;

    private String channel;

    private String ts;
    public void setType(ChannelMarkedType type) {
        this.type = type;
    }
    public ChannelMarkedType getType() {
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