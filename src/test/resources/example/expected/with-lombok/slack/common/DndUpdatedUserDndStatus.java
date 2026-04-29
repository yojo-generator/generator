package example.testGenerate.slack.common;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DndUpdatedUserDndStatus {

    private Boolean dndEnabled;

    private BigDecimal nextDndStartTs;

    private BigDecimal nextDndEndTs;

}