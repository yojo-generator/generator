package slack.messages;

import slack.common.FilePublicType;
import javax.validation.Valid;
import lombok.Data;
import slack.common.FilePublicFile;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was made public.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FilePublic {

    private FilePublicType type;

    private String fileId;

    @Valid
    private FilePublicFile file;

}