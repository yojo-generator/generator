package slack.messages;

import javax.validation.Valid;
import slack.common.ChannelCreatedType;
import slack.common.ChannelCreatedChannel;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A channel was created.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelCreated {

    private ChannelCreatedType type;

    @Valid
    private ChannelCreatedChannel channel;

}