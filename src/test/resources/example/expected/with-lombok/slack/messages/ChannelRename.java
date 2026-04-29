package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ChannelRenameChannel;
import lombok.Data;
import example.testGenerate.slack.common.ChannelRenameType;
import jakarta.validation.Valid;
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