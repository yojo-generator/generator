package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelLeftType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* You left a channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelLeft {

    private ChannelLeftType type;

    private String channel;

}