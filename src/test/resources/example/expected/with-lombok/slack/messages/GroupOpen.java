package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupOpenType;

@Generated("Yojo")
/**
* You opened a private channel.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupOpen {


    private GroupOpenType type;

    private String user;

    private String channel;
}