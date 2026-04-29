package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.BotChangedType;
import example.testGenerate.slack.common.BotChangedBot;
import lombok.Data;
import jakarta.validation.Valid;
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