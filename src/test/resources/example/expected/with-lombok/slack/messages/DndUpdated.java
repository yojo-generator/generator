package slack.messages;

import slack.common.DndUpdatedType;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import slack.common.DndUpdatedDndStatus;

/**
* Do not Disturb settings changed for the current user.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DndUpdated {

    private DndUpdatedType type;

    private String user;

    @Valid
    private DndUpdatedDndStatus dndStatus;

}