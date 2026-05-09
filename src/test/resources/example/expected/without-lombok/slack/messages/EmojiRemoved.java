package slack.messages;

import java.util.List;
import javax.annotation.processing.Generated;
import slack.common.EmojiRemovedSubtype;
import slack.common.EmojiRemovedType;

/**
* A custom emoji has been removed.
*/
@Generated("Yojo")
public class EmojiRemoved {


    private EmojiRemovedType type;

    private EmojiRemovedSubtype subtype;

    private List<Object> names;

    private String eventTs;
    public void setType(EmojiRemovedType type) {
        this.type = type;
    }
    public EmojiRemovedType getType() {
        return type;
    }
    public void setSubtype(EmojiRemovedSubtype subtype) {
        this.subtype = subtype;
    }
    public EmojiRemovedSubtype getSubtype() {
        return subtype;
    }
    public void setNames(List<Object> names) {
        this.names = names;
    }
    public List<Object> getNames() {
        return names;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}