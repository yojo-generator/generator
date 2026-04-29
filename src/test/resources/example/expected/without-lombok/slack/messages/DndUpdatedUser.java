package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.DndUpdatedUserType;
import example.testGenerate.slack.common.DndUpdatedUserDndStatus;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* Do not Disturb settings changed for a member.
*/
@Generated("Yojo")
public class DndUpdatedUser {

    private DndUpdatedUserType type;

    private String user;

    @Valid
    private DndUpdatedUserDndStatus dndStatus;

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