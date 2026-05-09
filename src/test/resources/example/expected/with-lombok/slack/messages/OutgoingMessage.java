package slack.messages;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.OutgoingMessageType;

/**
* A message was sent to a channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class OutgoingMessage {


    private BigDecimal id;

    private OutgoingMessageType type;

    private String channel;

    private String text;
}