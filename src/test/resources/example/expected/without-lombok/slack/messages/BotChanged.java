package slack.messages;

import slack.common.BotChangedType;
import javax.validation.Valid;
import slack.common.BotChangedBot;
import javax.annotation.processing.Generated;

/**
* A bot user was changed.
*/
@Generated("Yojo")
public class BotChanged {

    private BotChangedType type;

    @Valid
    private BotChangedBot bot;

    public void setBot(BotChangedBot bot) {
        this.bot = bot;
    }
    public BotChangedBot getBot() {
        return bot;
    }
}