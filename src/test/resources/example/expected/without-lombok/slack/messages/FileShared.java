package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileSharedFile;
import slack.common.FileSharedType;

@Generated("Yojo")
/**
* A file was shared.
*/
public class FileShared {


    private FileSharedType type;

    private String fileId;

    @Valid
    private FileSharedFile file;
    public void setType(FileSharedType type) {
        this.type = type;
    }
    public FileSharedType getType() {
        return type;
    }
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