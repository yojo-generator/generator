package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImOpenType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* You opened a DM.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImOpen {

    private ImOpenType type;

    private String channel;

    private String user;

}