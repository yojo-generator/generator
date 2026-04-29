package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImCreatedChannel;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import example.testGenerate.slack.common.ImCreatedType;

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