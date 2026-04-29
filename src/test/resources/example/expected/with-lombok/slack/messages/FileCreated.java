package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileCreatedFile;
import example.testGenerate.slack.common.FileCreatedType;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
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