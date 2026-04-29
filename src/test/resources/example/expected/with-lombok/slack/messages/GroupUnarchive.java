package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupUnarchiveType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A private channel was unarchived.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupUnarchive {

    private GroupUnarchiveType type;

    private String channel;

    private String user;

}