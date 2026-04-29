package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.DndUpdatedDndStatus;
import example.testGenerate.slack.common.DndUpdatedType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* Do not Disturb settings changed for the current user.
*/
@Generated("Yojo")
public class DndUpdated {

    private DndUpdatedType type;

    private String user;

    @Valid
    private DndUpdatedDndStatus dndStatus;

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    public void setDndStatus(DndUpdatedDndStatus dndStatus) {
        this.dndStatus = dndStatus;
    }
    public DndUpdatedDndStatus getDndStatus() {
        return dndStatus;
    }
}