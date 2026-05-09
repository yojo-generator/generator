package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileUnsharedFile;
import slack.common.FileUnsharedType;

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