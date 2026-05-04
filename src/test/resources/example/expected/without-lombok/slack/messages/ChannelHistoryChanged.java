package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ChannelHistoryChangedType;

@Generated("Yojo")
/**
* Bulk updates were made to a channel's history.
*/
public class ChannelHistoryChanged {


    private ChannelHistoryChangedType type;

    private String latest;

    private String ts;

    private String eventTs;
    public void setType(ChannelHistoryChangedType type) {
        this.type = type;
    }
    public ChannelHistoryChangedType getType() {
        return type;
    }
    public void setLatest(String latest) {
        this.latest = latest;
    }
    public String getLatest() {
        return latest;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getTs() {
        return ts;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}