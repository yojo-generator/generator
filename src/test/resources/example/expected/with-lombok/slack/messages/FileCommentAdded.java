package slack.messages;

import slack.common.FileCommentAddedFile;
import slack.common.FileCommentAddedType;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A file comment was added.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileCommentAdded {

    private FileCommentAddedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentAddedFile file;

}