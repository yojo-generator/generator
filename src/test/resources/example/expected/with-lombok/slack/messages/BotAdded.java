package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.BotAddedBot;
import slack.common.BotAddedType;

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