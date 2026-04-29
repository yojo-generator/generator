package slack.messages;

import slack.common.DndUpdatedType;
import javax.validation.Valid;
import javax.annotation.processing.Generated;
import slack.common.DndUpdatedDndStatus;

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