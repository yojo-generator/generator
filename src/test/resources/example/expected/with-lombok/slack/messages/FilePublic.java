package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FilePublicFile;
import slack.common.FilePublicType;

@Generated("Yojo")
/**
* A file was made public.
*/
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