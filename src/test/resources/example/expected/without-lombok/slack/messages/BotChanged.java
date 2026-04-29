package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.BotChangedType;
import example.testGenerate.slack.common.BotChangedBot;
import jakarta.validation.Valid;
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