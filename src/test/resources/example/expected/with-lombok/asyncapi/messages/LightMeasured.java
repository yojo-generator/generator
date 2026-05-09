package asyncapi.messages;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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