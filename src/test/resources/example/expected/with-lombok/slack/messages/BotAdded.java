package slack.messages;

import slack.common.BotAddedType;
import javax.validation.Valid;
import slack.common.BotAddedBot;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A bot user was added.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class BotAdded {

    private BotAddedType type;

    @Valid
    private BotAddedBot bot;

}