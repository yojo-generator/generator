package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelJoinedChannel;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import example.testGenerate.slack.common.ChannelJoinedType;
import lombok.NoArgsConstructor;

/**
* You joined a channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelJoined {

    private ChannelJoinedType type;

    @Valid
    private ChannelJoinedChannel channel;

}