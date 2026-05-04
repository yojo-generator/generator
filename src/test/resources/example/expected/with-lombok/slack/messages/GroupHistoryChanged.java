package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.GroupHistoryChangedType;

@Generated("Yojo")
/**
* Bulk updates were made to a private channel's history.
*/
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