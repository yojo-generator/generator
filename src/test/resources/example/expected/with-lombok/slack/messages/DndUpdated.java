package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.DndUpdatedDndStatus;
import example.testGenerate.slack.common.DndUpdatedType;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

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