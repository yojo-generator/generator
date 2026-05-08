package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCommentAddedFile;
import slack.common.FileCommentAddedType;

@Generated("Yojo")
/**
* A file comment was added.
*/
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