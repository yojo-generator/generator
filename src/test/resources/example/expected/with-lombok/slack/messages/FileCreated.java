package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCreatedFile;
import slack.common.FileCreatedType;

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