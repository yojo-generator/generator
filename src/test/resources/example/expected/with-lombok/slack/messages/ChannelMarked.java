package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelMarkedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Your channel read marker was updated.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelMarked {

    private ChannelMarkedType type;

    private String channel;

    private String ts;

}