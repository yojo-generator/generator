package slack.messages;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import slack.common.ImCloseType;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* You closed a DM.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImClose {

    private ImCloseType type;

    private String channel;

    private String user;

}