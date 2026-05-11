package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.BotChangedBot;
import slack.common.BotChangedType;

/**
* A bot user was changed.
*/
@Generated("Yojo")
public class BotChanged {


    private BotChangedType type;

    @Valid
    private BotChangedBot bot;
    public void setType(BotChangedType type) {
        this.type = type;
    }
    public BotChangedType getType() {
        return type;
    }
    public void setBot(BotChangedBot bot) {
        this.bot = bot;
    }
    public BotChangedBot getBot() {
        return bot;
    }
}