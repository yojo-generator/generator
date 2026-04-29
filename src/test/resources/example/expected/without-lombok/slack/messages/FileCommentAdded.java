package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileCommentAddedType;
import example.testGenerate.slack.common.FileCommentAddedFile;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* A file comment was added.
*/
@Generated("Yojo")
public class FileCommentAdded {

    private FileCommentAddedType type;

    private Object comment;

    private String fileId;

    @Valid
    private FileCommentAddedFile file;

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