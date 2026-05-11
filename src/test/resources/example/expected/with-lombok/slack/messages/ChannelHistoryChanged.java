package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ChannelHistoryChangedType;

/**
* Bulk updates were made to a channel's history.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ChannelHistoryChanged {


    private ChannelHistoryChangedType type;

    private String latest;

    private String ts;

    private String eventTs;
}