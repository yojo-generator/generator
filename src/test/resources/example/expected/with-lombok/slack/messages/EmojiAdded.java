package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.EmojiAddedSubtype;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import example.testGenerate.slack.common.EmojiAddedType;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.net.URI;

/**
* A custom emoji has been added.
*/
@Generated("Yojo")
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