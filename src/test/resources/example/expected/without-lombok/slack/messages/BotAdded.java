package slack.messages;

import slack.common.BotAddedType;
import javax.validation.Valid;
import slack.common.BotAddedBot;
import javax.annotation.processing.Generated;

/**
* A bot user was added.
*/
@Generated("Yojo")
public class BotAdded {

    private BotAddedType type;

    @Valid
    private BotAddedBot bot;

    public void setBot(BotAddedBot bot) {
        this.bot = bot;
    }
    public BotAddedBot getBot() {
        return bot;
    }
}