package slack.messages;

import slack.common.GroupHistoryChangedType;
import javax.annotation.processing.Generated;

/**
* Bulk updates were made to a private channel's history.
*/
@Generated("Yojo")
public class GroupHistoryChanged {

    private GroupHistoryChangedType type;

    private String latest;

    private String ts;

    private String eventTs;

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