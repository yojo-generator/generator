package slack.messages;

import slack.common.GroupMarkedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A private channel read marker was updated.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupMarked {

    private GroupMarkedType type;

    private String channel;

    private String ts;

}