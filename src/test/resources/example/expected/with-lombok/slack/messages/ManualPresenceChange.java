package slack.messages;

import slack.common.ManualPresenceChangeType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* You manually updated your presence.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ManualPresenceChange {

    private ManualPresenceChangeType type;

    private String presence;

}