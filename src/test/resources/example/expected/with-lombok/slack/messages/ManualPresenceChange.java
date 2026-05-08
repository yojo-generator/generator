package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ManualPresenceChangeType;

@Generated("Yojo")
/**
* You manually updated your presence.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ManualPresenceChange {


    private ManualPresenceChangeType type;

    private String presence;
}