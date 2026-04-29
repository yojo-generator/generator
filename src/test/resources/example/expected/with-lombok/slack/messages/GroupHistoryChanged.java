package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupHistoryChangedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Bulk updates were made to a private channel's history.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupHistoryChanged {

    private GroupHistoryChangedType type;

    private String latest;

    private String ts;

    private String eventTs;

}