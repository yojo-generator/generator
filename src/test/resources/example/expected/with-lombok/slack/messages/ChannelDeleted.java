package example.testGenerate.slack.messages;

import lombok.Data;
import example.testGenerate.slack.common.ChannelDeletedType;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A channel was deleted.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelDeleted {

    private ChannelDeletedType type;

    private String channel;

}