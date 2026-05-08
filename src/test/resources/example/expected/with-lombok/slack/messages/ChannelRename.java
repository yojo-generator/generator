package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ChannelRenameChannel;
import slack.common.ChannelRenameType;

@Generated("Yojo")
/**
* A channel was renamed.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelRename {


    private ChannelRenameType type;

    @Valid
    private ChannelRenameChannel channel;
}