package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.EmojiRemovedSubtype;
import java.util.List;
import example.testGenerate.slack.common.EmojiRemovedType;
import javax.annotation.processing.Generated;

/**
* A custom emoji has been removed.
*/
@Generated("Yojo")
public class EmojiRemoved {

    private EmojiRemovedType type;

    private EmojiRemovedSubtype subtype;

    private List<Object> names;

    private String eventTs;

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