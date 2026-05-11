package slack.common;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImCreatedChannel {


    private String id;

    private String name;

    private BigDecimal created;

    private String creator;
}