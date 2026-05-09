package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCommentDeletedFile;
import slack.common.FileCommentDeletedType;

/**
* A file comment was deleted.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileCommentDeleted {


    private FileCommentDeletedType type;

    private String comment;

    private String fileId;

    @Valid
    private FileCommentDeletedFile file;
}