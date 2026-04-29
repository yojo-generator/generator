package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileChangeType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.FileChangeFile;

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