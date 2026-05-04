package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.BotAddedBot;
import slack.common.BotAddedType;

@Generated("Yojo")
/**
* A bot user was added.
*/
public class BotAdded {


    private BotAddedType type;

    @Valid
    private BotAddedBot bot;
    public void setType(BotAddedType type) {
        this.type = type;
    }
    public BotAddedType getType() {
        return type;
    }
    public void setBot(BotAddedBot bot) {
        this.bot = bot;
    }
    public BotAddedBot getBot() {
        return bot;
    }
}