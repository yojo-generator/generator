package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCommentDeletedFile;
import slack.common.FileCommentDeletedType;

@Generated("Yojo")
/**
* A file comment was deleted.
*/
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