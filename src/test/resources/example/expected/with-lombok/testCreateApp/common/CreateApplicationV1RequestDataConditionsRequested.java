package testCreateApp.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
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
public class CreateApplicationV1RequestDataConditionsRequested {

    @NotBlank
    private String purchaseNumber;

    @NotBlank
    private String bgSubtype;

    @NotNull
    private BigDecimal sum;

    @NotNull
    private OffsetDateTime endDate;

}