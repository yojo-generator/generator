package example.testGenerate.test.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* Here was located supported object types
*/
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