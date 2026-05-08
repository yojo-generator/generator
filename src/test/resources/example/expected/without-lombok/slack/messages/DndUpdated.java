package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.DndUpdatedDndStatus;
import slack.common.DndUpdatedType;

@Generated("Yojo")
/**
* Do not Disturb settings changed for the current user.
*/
public class DndUpdated {


    private DndUpdatedType type;

    private String user;

    @Valid
    private DndUpdatedDndStatus dndStatus;
    public void setType(DndUpdatedType type) {
        this.type = type;
    }
    public DndUpdatedType getType() {
        return type;
    }
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