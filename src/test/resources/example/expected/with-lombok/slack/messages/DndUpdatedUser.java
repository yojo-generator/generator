package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.DndUpdatedUserDndStatus;
import slack.common.DndUpdatedUserType;

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