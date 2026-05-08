package asyncapi.common;

import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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