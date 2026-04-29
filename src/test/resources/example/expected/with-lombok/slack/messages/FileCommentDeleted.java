package slack.messages;

import slack.common.FileCommentDeletedFile;
import javax.validation.Valid;
import slack.common.FileCommentDeletedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

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