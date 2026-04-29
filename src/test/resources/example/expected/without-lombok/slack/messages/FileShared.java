package slack.messages;

import slack.common.FileSharedFile;
import javax.validation.Valid;
import slack.common.FileSharedType;
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