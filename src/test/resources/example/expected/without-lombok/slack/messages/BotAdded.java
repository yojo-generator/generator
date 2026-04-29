package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.BotAddedType;
import example.testGenerate.slack.common.BotAddedBot;
import jakarta.validation.Valid;
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