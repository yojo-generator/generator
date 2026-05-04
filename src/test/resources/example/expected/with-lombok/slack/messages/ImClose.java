package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ImCloseType;

@Generated("Yojo")
/**
* You closed a DM.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImClose {


    private ImCloseType type;

    private String channel;

    private String user;
}