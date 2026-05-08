package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileCommentAddedFile;
import slack.common.FileCommentAddedType;

@Generated("Yojo")
/**
* A file comment was added.
*/
public class FileCommentAdded {


    private FileCommentAddedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentAddedFile file;
    public void setType(FileCommentAddedType type) {
        this.type = type;
    }
    public FileCommentAddedType getType() {
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
    public void setFile(FileCommentAddedFile file) {
        this.file = file;
    }
    public FileCommentAddedFile getFile() {
        return file;
    }
}