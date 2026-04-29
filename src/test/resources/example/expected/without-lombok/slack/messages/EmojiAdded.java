package slack.messages;

import slack.common.EmojiAddedSubtype;
import slack.common.EmojiAddedType;
import javax.annotation.processing.Generated;
import java.net.URI;

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