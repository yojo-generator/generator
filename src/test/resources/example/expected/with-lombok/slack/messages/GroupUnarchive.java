package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupUnarchiveType;

@Generated("Yojo")
/**
* A private channel was unarchived.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupUnarchive {


    private GroupUnarchiveType type;

    private String channel;

    private String user;
}