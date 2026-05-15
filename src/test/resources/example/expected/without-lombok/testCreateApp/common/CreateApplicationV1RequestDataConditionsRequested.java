package testCreateApp.common;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Generated("Yojo")
public class CreateApplicationV1RequestDataConditionsRequested {


    @NotBlank
    private String purchaseNumber;

    @NotBlank
    private String bgSubtype;

    @NotNull
    private BigDecimal sum;

    @NotNull
    private OffsetDateTime endDate;
    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
    public String getPurchaseNumber() {
        return purchaseNumber;
    }
    public void setBgSubtype(String bgSubtype) {
        this.bgSubtype = bgSubtype;
    }
    public String getBgSubtype() {
        return bgSubtype;
    }
    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
    public BigDecimal getSum() {
        return sum;
    }
    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }
    public OffsetDateTime getEndDate() {
        return endDate;
    }
    @Override
    public String toString() {
        return "CreateApplicationV1RequestDataConditionsRequested{" +
                "purchaseNumber=" + purchaseNumber + ", " +
                "bgSubtype=" + bgSubtype + ", " +
                "sum=" + sum + ", " +
                "endDate=" + endDate +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateApplicationV1RequestDataConditionsRequested that = (CreateApplicationV1RequestDataConditionsRequested) o;
        return Objects.equals(purchaseNumber, that.purchaseNumber) &&
                Objects.equals(bgSubtype, that.bgSubtype) &&
                Objects.equals(sum, that.sum) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseNumber, bgSubtype, sum, endDate);
    }
}