package slack.messages;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import slack.common.ImOpenType;

/**
* You opened a DM.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImOpen {

    private ImOpenType type;

    private String channel;

    private String user;

}