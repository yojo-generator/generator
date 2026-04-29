package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileCommentEditedFile;
import example.testGenerate.slack.common.FileCommentEditedType;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file comment was edited.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileCommentEdited {

    private FileCommentEditedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentEditedFile file;

}