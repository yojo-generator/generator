package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileSharedFile;
import slack.common.FileSharedType;

@Generated("Yojo")
/**
* A file was shared.
*/
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