package example.testGenerate.test.common;

import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Here was located supported object types
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ObjectTypes {

    private UUID uuidValue;

    private UUID uuidWithDefaultValue = UUID.fromString("a1256d5f-4ce6-4bb6-9fd1-ae265fe186db");

    @NotNull
    private UUID uuidValueWithRequired;

    private Date dateValue;

    @NotNull
    private Date dateValueWithRequired = new Date();

    private LocalDate localDateValue;

    @NotNull
    private LocalDate localDateValueWithRequired;

    private LocalDateTime localDateTimeValue;

    @NotNull
    private LocalDateTime localDateTimeValueWithRequired;

    private OffsetDateTime offsetDateTimeValue;

    @NotNull
    private OffsetDateTime offsetDateTimeValueWithRequired;

}