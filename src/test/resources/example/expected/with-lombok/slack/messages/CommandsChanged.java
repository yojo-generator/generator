package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.CommandsChangedType;

@Generated("Yojo")
/**
* A slash command has been added or changed.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CommandsChanged {


    private CommandsChangedType type;

    private String eventTs;
}