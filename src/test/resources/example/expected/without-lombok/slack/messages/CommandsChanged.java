package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.CommandsChangedType;

@Generated("Yojo")
/**
* A slash command has been added or changed.
*/
public class CommandsChanged {


    private CommandsChangedType type;

    private String eventTs;
    public void setType(CommandsChangedType type) {
        this.type = type;
    }
    public CommandsChangedType getType() {
        return type;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}