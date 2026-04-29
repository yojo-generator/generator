package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ManualPresenceChangeType;
import javax.annotation.processing.Generated;

/**
* You manually updated your presence.
*/
@Generated("Yojo")
public class ManualPresenceChange {

    private ManualPresenceChangeType type;

    private String presence;

    public void setPresence(String presence) {
        this.presence = presence;
    }
    public String getPresence() {
        return presence;
    }
}