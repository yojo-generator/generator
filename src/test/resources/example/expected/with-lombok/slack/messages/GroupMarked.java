package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupMarkedType;

@Generated("Yojo")
/**
* A private channel read marker was updated.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupMarked {


    private GroupMarkedType type;

    private String channel;

    private String ts;
}