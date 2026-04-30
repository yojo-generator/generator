package slack.messages;

import slack.common.BotChangedType;
import javax.validation.Valid;
import lombok.Data;
import slack.common.BotChangedBot;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A bot user was changed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class BotChanged {

    private BotChangedType type;

    @Valid
    private BotChangedBot bot;

}