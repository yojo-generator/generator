package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GroupHistoryChangedType;

@Generated("Yojo")
/**
* Bulk updates were made to a private channel's history.
*/
public class GroupHistoryChanged {


    private GroupHistoryChangedType type;

    private String latest;

    private String ts;

    private String eventTs;
    public void setType(GroupHistoryChangedType type) {
        this.type = type;
    }
    public GroupHistoryChangedType getType() {
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