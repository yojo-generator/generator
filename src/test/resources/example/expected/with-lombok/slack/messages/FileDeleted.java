package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileDeletedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was deleted.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileDeleted {

    private FileDeletedType type;

    private String fileId;

    private String eventTs;

}