package slack.messages;

import java.net.URI;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.EmojiAddedSubtype;
import slack.common.EmojiAddedType;

@Generated("Yojo")
/**
* A custom emoji has been added.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class EmojiAdded {


    private EmojiAddedType type;

    private EmojiAddedSubtype subtype;

    private String name;

    private URI value;

    private String eventTs;
}