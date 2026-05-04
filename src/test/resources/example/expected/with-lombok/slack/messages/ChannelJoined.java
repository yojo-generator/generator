package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ChannelJoinedChannel;
import slack.common.ChannelJoinedType;

@Generated("Yojo")
/**
* You joined a channel.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelJoined {


    private ChannelJoinedType type;

    @Valid
    private ChannelJoinedChannel channel;
}