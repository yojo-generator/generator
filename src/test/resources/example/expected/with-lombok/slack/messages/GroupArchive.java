package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupArchiveType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A private channel was archived.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GroupArchive {

    private GroupArchiveType type;

    private String channel;

}