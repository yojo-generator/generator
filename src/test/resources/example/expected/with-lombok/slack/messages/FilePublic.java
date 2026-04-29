package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FilePublicFile;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import example.testGenerate.slack.common.FilePublicType;

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