package slack.messages;

import slack.common.ImCreatedType;
import javax.validation.Valid;
import slack.common.ImCreatedChannel;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A DM was created.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ImCreated {

    private ImCreatedType type;

    @Valid
    private ImCreatedChannel channel;

    private String user;

}