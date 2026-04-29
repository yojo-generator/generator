package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.DndUpdatedUserType;
import example.testGenerate.slack.common.DndUpdatedUserDndStatus;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
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