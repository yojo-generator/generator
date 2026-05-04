package slack.messages;

import java.util.List;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.EmojiRemovedSubtype;
import slack.common.EmojiRemovedType;

@Generated("Yojo")
/**
* A custom emoji has been removed.
*/
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