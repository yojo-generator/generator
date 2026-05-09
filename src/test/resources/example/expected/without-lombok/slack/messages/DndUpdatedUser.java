package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.DndUpdatedUserDndStatus;
import slack.common.DndUpdatedUserType;

/**
* Do not Disturb settings changed for a member.
*/
@Generated("Yojo")
public class DndUpdatedUser {


    private DndUpdatedUserType type;

    private String user;

    @Valid
    private DndUpdatedUserDndStatus dndStatus;
    public void setType(DndUpdatedUserType type) {
        this.type = type;
    }
    public DndUpdatedUserType getType() {
        return type;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    public void setDndStatus(DndUpdatedUserDndStatus dndStatus) {
        this.dndStatus = dndStatus;
    }
    public DndUpdatedUserDndStatus getDndStatus() {
        return dndStatus;
    }
}