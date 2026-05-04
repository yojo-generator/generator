package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileCommentEditedFile;
import slack.common.FileCommentEditedType;

@Generated("Yojo")
/**
* A file comment was edited.
*/
public class FileCommentEdited {


    private FileCommentEditedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentEditedFile file;
    public void setType(FileCommentEditedType type) {
        this.type = type;
    }
    public FileCommentEditedType getType() {
        return type;
    }
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