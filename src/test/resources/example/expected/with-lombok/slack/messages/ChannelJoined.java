package slack.messages;

import slack.common.ChannelJoinedType;
import slack.common.ChannelJoinedChannel;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
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