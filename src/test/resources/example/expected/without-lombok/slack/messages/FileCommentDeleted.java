package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileCommentDeletedFile;
import slack.common.FileCommentDeletedType;

/**
* A file comment was deleted.
*/
@Generated("Yojo")
public class FileCommentDeleted {


    private FileCommentDeletedType type;

    private String comment;

    private String fileId;

    @Valid
    private FileCommentDeletedFile file;
    public void setType(FileCommentDeletedType type) {
        this.type = type;
    }
    public FileCommentDeletedType getType() {
        return type;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileCommentDeletedFile file) {
        this.file = file;
    }
    public FileCommentDeletedFile getFile() {
        return file;
    }
}