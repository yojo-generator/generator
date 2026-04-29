package slack.messages;

import slack.common.FileSharedFile;
import javax.validation.Valid;
import lombok.Data;
import slack.common.FileSharedType;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was shared.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileShared {

    private FileSharedType type;

    private String fileId;

    @Valid
    private FileSharedFile file;

}