package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.BotChangedBot;
import slack.common.BotChangedType;

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