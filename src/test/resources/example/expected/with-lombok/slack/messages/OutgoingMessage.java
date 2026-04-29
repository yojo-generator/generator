package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.OutgoingMessageType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

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