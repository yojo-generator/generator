package slack.messages;

import slack.common.FileChangeFile;
import javax.validation.Valid;
import slack.common.FileChangeType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was changed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileChange {

    private FileChangeType type;

    private String fileId;

    @Valid
    private FileChangeFile file;

}