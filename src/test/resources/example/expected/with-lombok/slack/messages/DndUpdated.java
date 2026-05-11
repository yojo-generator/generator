package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.DndUpdatedDndStatus;
import slack.common.DndUpdatedType;

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