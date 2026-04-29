package slack.messages;

import slack.common.GroupJoinedChannel;
import slack.common.GroupJoinedType;
import javax.validation.Valid;
import lombok.Data;
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