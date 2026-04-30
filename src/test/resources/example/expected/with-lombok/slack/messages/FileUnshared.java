package slack.messages;

import slack.common.FileUnsharedType;
import slack.common.FileUnsharedFile;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was unshared.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileUnshared {

    private FileUnsharedType type;

    private String fileId;

    @Valid
    private FileUnsharedFile file;

}