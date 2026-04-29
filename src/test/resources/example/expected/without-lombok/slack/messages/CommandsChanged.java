package slack.messages;

import slack.common.CommandsChangedType;
import javax.annotation.processing.Generated;

/**
* A slash command has been added or changed.
*/
@Generated("Yojo")
public class CommandsChanged {

    private CommandsChangedType type;

    private String eventTs;

    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}