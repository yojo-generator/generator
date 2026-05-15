package slack.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class DndUpdatedDndStatus {


    private Boolean dndEnabled;

    private BigDecimal nextDndStartTs;

    private BigDecimal nextDndEndTs;

    private Boolean snoozeEnabled;

    private BigDecimal snoozeEndtime;
    public void setDndEnabled(Boolean dndEnabled) {
        this.dndEnabled = dndEnabled;
    }
    public Boolean getDndEnabled() {
        return dndEnabled;
    }
    public void setNextDndStartTs(BigDecimal nextDndStartTs) {
        this.nextDndStartTs = nextDndStartTs;
    }
    public BigDecimal getNextDndStartTs() {
        return nextDndStartTs;
    }
    public void setNextDndEndTs(BigDecimal nextDndEndTs) {
        this.nextDndEndTs = nextDndEndTs;
    }
    public BigDecimal getNextDndEndTs() {
        return nextDndEndTs;
    }
    public void setSnoozeEnabled(Boolean snoozeEnabled) {
        this.snoozeEnabled = snoozeEnabled;
    }
    public Boolean getSnoozeEnabled() {
        return snoozeEnabled;
    }
    public void setSnoozeEndtime(BigDecimal snoozeEndtime) {
        this.snoozeEndtime = snoozeEndtime;
    }
    public BigDecimal getSnoozeEndtime() {
        return snoozeEndtime;
    }
    @Override
    public String toString() {
        return "DndUpdatedDndStatus{" +
                "dndEnabled=" + dndEnabled + ", " +
                "nextDndStartTs=" + nextDndStartTs + ", " +
                "nextDndEndTs=" + nextDndEndTs + ", " +
                "snoozeEnabled=" + snoozeEnabled + ", " +
                "snoozeEndtime=" + snoozeEndtime +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DndUpdatedDndStatus that = (DndUpdatedDndStatus) o;
        return Objects.equals(dndEnabled, that.dndEnabled) &&
                Objects.equals(nextDndStartTs, that.nextDndStartTs) &&
                Objects.equals(nextDndEndTs, that.nextDndEndTs) &&
                Objects.equals(snoozeEnabled, that.snoozeEnabled) &&
                Objects.equals(snoozeEndtime, that.snoozeEndtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dndEnabled, nextDndStartTs, nextDndEndTs, snoozeEnabled, snoozeEndtime);
    }
}