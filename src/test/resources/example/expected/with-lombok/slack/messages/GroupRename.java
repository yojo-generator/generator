package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupRenameChannel;
import example.testGenerate.slack.common.GroupRenameType;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A private channel was renamed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupRename {

    private GroupRenameType type;

    @Valid
    private GroupRenameChannel channel;

}