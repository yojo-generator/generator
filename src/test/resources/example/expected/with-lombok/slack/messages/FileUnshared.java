package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileUnsharedFile;
import example.testGenerate.slack.common.FileUnsharedType;
import lombok.Data;
import jakarta.validation.Valid;
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