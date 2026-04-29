package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileSharedFile;
import example.testGenerate.slack.common.FileSharedType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

/**
* A file was shared.
*/
@Generated("Yojo")
public class FileShared {

    private FileSharedType type;

    private String fileId;

    @Valid
    private FileSharedFile file;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileSharedFile file) {
        this.file = file;
    }
    public FileSharedFile getFile() {
        return file;
    }
}