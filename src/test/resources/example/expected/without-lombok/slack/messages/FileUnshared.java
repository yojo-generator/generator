package slack.messages;

import slack.common.FileUnsharedType;
import slack.common.FileUnsharedFile;
import javax.validation.Valid;
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