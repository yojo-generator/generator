package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileUnsharedFile;
import slack.common.FileUnsharedType;

/**
* A file was unshared.
*/
@Generated("Yojo")
public class FileUnshared {


    private FileUnsharedType type;

    private String fileId;

    @Valid
    private FileUnsharedFile file;
    public void setType(FileUnsharedType type) {
        this.type = type;
    }
    public FileUnsharedType getType() {
        return type;
    }
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