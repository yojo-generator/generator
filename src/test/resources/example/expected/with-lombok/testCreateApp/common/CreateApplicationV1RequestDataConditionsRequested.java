package testCreateApp.common;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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