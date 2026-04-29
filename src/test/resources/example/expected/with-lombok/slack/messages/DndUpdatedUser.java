package slack.messages;

import slack.common.DndUpdatedUserType;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import slack.common.DndUpdatedUserDndStatus;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Do not Disturb settings changed for a member.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DndUpdatedUser {

    private DndUpdatedUserType type;

    private String user;

    @Valid
    private DndUpdatedUserDndStatus dndStatus;

}