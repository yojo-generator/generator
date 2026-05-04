package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileChangeFile;
import slack.common.FileChangeType;

@Generated("Yojo")
/**
* A file was changed.
*/
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