package example.testGenerate.test.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;

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
    @Override
    public String toString() {
        return "ObjectTypes{" +
                "uuidValue=" + uuidValue + ", " +
                "uuidWithDefaultValue=" + uuidWithDefaultValue + ", " +
                "uuidValueWithRequired=" + uuidValueWithRequired + ", " +
                "dateValue=" + dateValue + ", " +
                "dateValueWithRequired=" + dateValueWithRequired + ", " +
                "localDateValue=" + localDateValue + ", " +
                "localDateValueWithRequired=" + localDateValueWithRequired + ", " +
                "localDateTimeValue=" + localDateTimeValue + ", " +
                "localDateTimeValueWithRequired=" + localDateTimeValueWithRequired + ", " +
                "offsetDateTimeValue=" + offsetDateTimeValue + ", " +
                "offsetDateTimeValueWithRequired=" + offsetDateTimeValueWithRequired +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectTypes that = (ObjectTypes) o;
        return Objects.equals(uuidValue, that.uuidValue) &&
                Objects.equals(uuidWithDefaultValue, that.uuidWithDefaultValue) &&
                Objects.equals(uuidValueWithRequired, that.uuidValueWithRequired) &&
                Objects.equals(dateValue, that.dateValue) &&
                Objects.equals(dateValueWithRequired, that.dateValueWithRequired) &&
                Objects.equals(localDateValue, that.localDateValue) &&
                Objects.equals(localDateValueWithRequired, that.localDateValueWithRequired) &&
                Objects.equals(localDateTimeValue, that.localDateTimeValue) &&
                Objects.equals(localDateTimeValueWithRequired, that.localDateTimeValueWithRequired) &&
                Objects.equals(offsetDateTimeValue, that.offsetDateTimeValue) &&
                Objects.equals(offsetDateTimeValueWithRequired, that.offsetDateTimeValueWithRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuidValue, uuidWithDefaultValue, uuidValueWithRequired, dateValue, dateValueWithRequired, localDateValue, localDateValueWithRequired, localDateTimeValue, localDateTimeValueWithRequired, offsetDateTimeValue, offsetDateTimeValueWithRequired);
    }
}