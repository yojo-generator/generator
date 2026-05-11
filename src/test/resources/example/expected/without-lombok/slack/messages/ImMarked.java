package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ImMarkedType;

/**
* A direct message read marker was updated.
*/
@Generated("Yojo")
public class ImMarked {


    private ImMarkedType type;

    private String channel;

    private String ts;
    public void setType(ImMarkedType type) {
        this.type = type;
    }
    public ImMarkedType getType() {
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