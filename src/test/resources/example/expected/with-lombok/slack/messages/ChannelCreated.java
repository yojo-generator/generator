package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ChannelCreatedChannel;
import slack.common.ChannelCreatedType;

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