package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileUnsharedFile;
import example.testGenerate.slack.common.FileUnsharedType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* A file was unshared.
*/
@Generated("Yojo")
public class FileUnshared {

    private FileUnsharedType type;

    private String fileId;

    @Valid
    private FileUnsharedFile file;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileUnsharedFile file) {
        this.file = file;
    }
    public FileUnsharedFile getFile() {
        return file;
    }
}