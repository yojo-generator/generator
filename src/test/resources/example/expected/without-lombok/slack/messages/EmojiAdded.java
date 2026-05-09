package slack.messages;

import java.net.URI;
import javax.annotation.processing.Generated;
import slack.common.EmojiAddedSubtype;
import slack.common.EmojiAddedType;

/**
* A custom emoji has been added.
*/
@Generated("Yojo")
public class EmojiAdded {


    private EmojiAddedType type;

    private EmojiAddedSubtype subtype;

    private String name;

    private URI value;

    private String eventTs;
    public void setType(EmojiAddedType type) {
        this.type = type;
    }
    public EmojiAddedType getType() {
        return type;
    }
    public void setSubtype(EmojiAddedSubtype subtype) {
        this.subtype = subtype;
    }
    public EmojiAddedSubtype getSubtype() {
        return subtype;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setValue(URI value) {
        this.value = value;
    }
    public URI getValue() {
        return value;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}