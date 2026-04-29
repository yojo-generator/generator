package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileSharedFile;
import example.testGenerate.slack.common.FileSharedType;
import lombok.Data;
import jakarta.validation.Valid;
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