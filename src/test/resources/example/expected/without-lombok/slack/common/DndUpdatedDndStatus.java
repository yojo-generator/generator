package slack.common;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

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
}