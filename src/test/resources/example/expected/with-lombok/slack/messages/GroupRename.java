package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupRenameChannel;
import slack.common.GroupRenameType;

@Generated("Yojo")
/**
* A private channel was renamed.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupRename {


    private GroupRenameType type;

    @Valid
    private GroupRenameChannel channel;
}