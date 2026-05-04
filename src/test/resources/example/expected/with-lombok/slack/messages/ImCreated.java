package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ImCreatedChannel;
import slack.common.ImCreatedType;

@Generated("Yojo")
/**
* A DM was created.
*/
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