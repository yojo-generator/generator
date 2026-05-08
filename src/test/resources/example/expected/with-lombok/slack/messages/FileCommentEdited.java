package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileCommentEditedFile;
import slack.common.FileCommentEditedType;

@Generated("Yojo")
/**
* A file comment was edited.
*/
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