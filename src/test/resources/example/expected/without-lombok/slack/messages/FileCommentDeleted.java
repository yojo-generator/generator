package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileCommentDeletedType;
import example.testGenerate.slack.common.FileCommentDeletedFile;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

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