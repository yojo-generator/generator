package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.BotAddedType;
import lombok.Data;
import example.testGenerate.slack.common.BotAddedBot;
import jakarta.validation.Valid;
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