package asyncapi.messages;

import asyncapi.common.TurnOnOffPayloadCommand;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* Command a particular streetlight to turn the lights on or off.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class TurnOnOff {


    /**
     * Whether to turn on or off the light.
     */
    private TurnOnOffPayloadCommand command;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;
}