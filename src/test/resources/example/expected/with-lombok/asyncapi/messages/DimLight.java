package asyncapi.messages;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* Command a particular streetlight to dim the lights.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DimLight {


    /**
     * Percentage to which the light should be dimmed to.
     */
    @Min(0)
    @Max(100)
    private Integer percentage;

    /**
     * Date and time when the message was sent.
     */
    private OffsetDateTime sentAt;
}