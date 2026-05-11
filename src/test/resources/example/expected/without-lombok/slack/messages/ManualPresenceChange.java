package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.ManualPresenceChangeType;

/**
* You manually updated your presence.
*/
@Generated("Yojo")
public class ManualPresenceChange {


    private ManualPresenceChangeType type;

    private String presence;
    public void setType(ManualPresenceChangeType type) {
        this.type = type;
    }
    public ManualPresenceChangeType getType() {
        return type;
    }
    public void setPresence(String presence) {
        this.presence = presence;
    }
    public String getPresence() {
        return presence;
    }
}