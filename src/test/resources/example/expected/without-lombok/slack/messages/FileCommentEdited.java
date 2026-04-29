package slack.messages;

import slack.common.FileCommentEditedType;
import javax.validation.Valid;
import slack.common.FileCommentEditedFile;
import javax.annotation.processing.Generated;

/**
* A file comment was edited.
*/
@Generated("Yojo")
public class FileCommentEdited {

    private FileCommentEditedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentEditedFile file;

    public void setComment(Object comment) {
        this.comment = comment;
    }
    public Object getComment() {
        return comment;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileCommentEditedFile file) {
        this.file = file;
    }
    public FileCommentEditedFile getFile() {
        return file;
    }
}