package slack.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

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
    @Override
    public String toString() {
        return "DndUpdatedUserDndStatus{" +
                "dndEnabled=" + dndEnabled + ", " +
                "nextDndStartTs=" + nextDndStartTs + ", " +
                "nextDndEndTs=" + nextDndEndTs +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DndUpdatedUserDndStatus that = (DndUpdatedUserDndStatus) o;
        return Objects.equals(dndEnabled, that.dndEnabled) &&
                Objects.equals(nextDndStartTs, that.nextDndStartTs) &&
                Objects.equals(nextDndEndTs, that.nextDndEndTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dndEnabled, nextDndStartTs, nextDndEndTs);
    }
}