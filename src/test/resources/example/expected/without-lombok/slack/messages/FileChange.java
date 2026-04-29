package slack.messages;

import slack.common.FileChangeFile;
import javax.validation.Valid;
import slack.common.FileChangeType;
import javax.annotation.processing.Generated;

/**
* A file was changed.
*/
@Generated("Yojo")
public class FileChange {

    private FileChangeType type;

    private String fileId;

    @Valid
    private FileChangeFile file;

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