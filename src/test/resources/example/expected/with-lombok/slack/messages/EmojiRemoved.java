package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.EmojiRemovedSubtype;
import java.util.List;
import example.testGenerate.slack.common.EmojiRemovedType;
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