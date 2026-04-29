package asyncapi.messages;

import java.time.OffsetDateTime;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;

/**
* Inform about environmental lighting conditions of a particular streetlight.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class LightMeasured {

    /**
     * Light intensity measured in lumens.
     */
    @Min(0)
    private Integer lumens;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;

}