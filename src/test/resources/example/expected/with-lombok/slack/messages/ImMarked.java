package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ImMarkedType;

@Generated("Yojo")
/**
* A direct message read marker was updated.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImMarked {


    private ImMarkedType type;

    private String channel;

    private String ts;
}