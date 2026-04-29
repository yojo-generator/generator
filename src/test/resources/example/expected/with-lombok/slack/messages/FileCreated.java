package slack.messages;

import javax.validation.Valid;
import slack.common.FileCreatedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import slack.common.FileCreatedFile;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was created.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileCreated {

    private FileCreatedType type;

    private String fileId;

    @Valid
    private FileCreatedFile file;

}