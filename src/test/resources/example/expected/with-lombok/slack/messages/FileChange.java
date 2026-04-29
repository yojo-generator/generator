package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileChangeType;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import example.testGenerate.slack.common.FileChangeFile;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file was changed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileChange {

    private FileChangeType type;

    private String fileId;

    @Valid
    private FileChangeFile file;

}