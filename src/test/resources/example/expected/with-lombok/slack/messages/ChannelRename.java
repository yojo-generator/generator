package slack.messages;

import slack.common.ChannelRenameType;
import slack.common.ChannelRenameChannel;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A channel was renamed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelRename {

    private ChannelRenameType type;

    @Valid
    private ChannelRenameChannel channel;

}