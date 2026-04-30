package slack.messages;

import java.util.List;
import slack.common.EmojiRemovedType;
import slack.common.EmojiRemovedSubtype;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A custom emoji has been removed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class EmojiRemoved {

    private EmojiRemovedType type;

    private EmojiRemovedSubtype subtype;

    private List<Object> names;

    private String eventTs;

}