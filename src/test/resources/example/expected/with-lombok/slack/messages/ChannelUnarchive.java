package slack.messages;

import slack.common.ChannelUnarchiveType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A channel was unarchived.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelUnarchive {

    private ChannelUnarchiveType type;

    private String channel;

    private String user;

}