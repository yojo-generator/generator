package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCommentAddedFile;
import slack.common.FileCommentAddedType;

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