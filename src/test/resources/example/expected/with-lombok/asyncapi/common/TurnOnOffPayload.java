package asyncapi.common;

import java.time.OffsetDateTime;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import asyncapi.common.TurnOnOffPayloadCommand;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class TurnOnOffPayload {

    /**
     * Whether to turn on or off the light.
     */
    private TurnOnOffPayloadCommand command;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;

}