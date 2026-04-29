package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupJoinedType;
import example.testGenerate.slack.common.GroupJoinedChannel;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* You joined a private channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupJoined {

    private GroupJoinedType type;

    @Valid
    private GroupJoinedChannel channel;

}