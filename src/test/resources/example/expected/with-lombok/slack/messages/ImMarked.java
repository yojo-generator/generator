package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImMarkedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A direct message read marker was updated.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImMarked {

    private ImMarkedType type;

    private String channel;

    private String ts;

}