package example.testGenerate.slack.common;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Generated("Yojo")
public class DndUpdatedUserDndStatus {

    private Boolean dndEnabled;

    private BigDecimal nextDndStartTs;

    private BigDecimal nextDndEndTs;

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
}