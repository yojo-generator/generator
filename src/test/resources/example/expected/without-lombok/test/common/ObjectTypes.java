package example.testGenerate.test.common;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;

/**
* Here was located supported object types
*/
@Generated("Yojo")
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

    public void setUuidValue(UUID uuidValue) {
        this.uuidValue = uuidValue;
    }
    public UUID getUuidValue() {
        return uuidValue;
    }
    public void setUuidWithDefaultValue(UUID uuidWithDefaultValue) {
        this.uuidWithDefaultValue = uuidWithDefaultValue;
    }
    public UUID getUuidWithDefaultValue() {
        return uuidWithDefaultValue;
    }
    public void setUuidValueWithRequired(UUID uuidValueWithRequired) {
        this.uuidValueWithRequired = uuidValueWithRequired;
    }
    public UUID getUuidValueWithRequired() {
        return uuidValueWithRequired;
    }
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
    public Date getDateValue() {
        return dateValue;
    }
    public void setDateValueWithRequired(Date dateValueWithRequired) {
        this.dateValueWithRequired = dateValueWithRequired;
    }
    public Date getDateValueWithRequired() {
        return dateValueWithRequired;
    }
    public void setLocalDateValue(LocalDate localDateValue) {
        this.localDateValue = localDateValue;
    }
    public LocalDate getLocalDateValue() {
        return localDateValue;
    }
    public void setLocalDateValueWithRequired(LocalDate localDateValueWithRequired) {
        this.localDateValueWithRequired = localDateValueWithRequired;
    }
    public LocalDate getLocalDateValueWithRequired() {
        return localDateValueWithRequired;
    }
    public void setLocalDateTimeValue(LocalDateTime localDateTimeValue) {
        this.localDateTimeValue = localDateTimeValue;
    }
    public LocalDateTime getLocalDateTimeValue() {
        return localDateTimeValue;
    }
    public void setLocalDateTimeValueWithRequired(LocalDateTime localDateTimeValueWithRequired) {
        this.localDateTimeValueWithRequired = localDateTimeValueWithRequired;
    }
    public LocalDateTime getLocalDateTimeValueWithRequired() {
        return localDateTimeValueWithRequired;
    }
    public void setOffsetDateTimeValue(OffsetDateTime offsetDateTimeValue) {
        this.offsetDateTimeValue = offsetDateTimeValue;
    }
    public OffsetDateTime getOffsetDateTimeValue() {
        return offsetDateTimeValue;
    }
    public void setOffsetDateTimeValueWithRequired(OffsetDateTime offsetDateTimeValueWithRequired) {
        this.offsetDateTimeValueWithRequired = offsetDateTimeValueWithRequired;
    }
    public OffsetDateTime getOffsetDateTimeValueWithRequired() {
        return offsetDateTimeValueWithRequired;
    }
}