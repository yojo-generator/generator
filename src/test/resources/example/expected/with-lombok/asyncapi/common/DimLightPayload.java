package asyncapi.common;

import java.time.OffsetDateTime;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import javax.validation.constraints.Max;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DimLightPayload {

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