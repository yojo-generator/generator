package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupJoinedChannel;
import slack.common.GroupJoinedType;

@Generated("Yojo")
/**
* You joined a private channel.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupJoined {


    private GroupJoinedType type;

    @Valid
    private GroupJoinedChannel channel;
}