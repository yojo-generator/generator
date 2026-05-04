package testCreateApp.common;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
}