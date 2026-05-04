package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileChangeFile;
import slack.common.FileChangeType;

@Generated("Yojo")
/**
* A file was changed.
*/
public class FileChange {


    private FileChangeType type;

    private String fileId;

    @Valid
    private FileChangeFile file;
    public void setType(FileChangeType type) {
        this.type = type;
    }
    public FileChangeType getType() {
        return type;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileChangeFile file) {
        this.file = file;
    }
    public FileChangeFile getFile() {
        return file;
    }
}