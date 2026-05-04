package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupCloseType;

@Generated("Yojo")
/**
* You closed a private channel.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupClose {


    private GroupCloseType type;

    private String user;

    private String channel;
}