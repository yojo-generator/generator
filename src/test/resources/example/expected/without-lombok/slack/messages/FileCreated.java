package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FileCreatedFile;
import slack.common.FileCreatedType;

@Generated("Yojo")
/**
* A file was created.
*/
public class FileCreated {


    private FileCreatedType type;

    private String fileId;

    @Valid
    private FileCreatedFile file;
    public void setType(FileCreatedType type) {
        this.type = type;
    }
    public FileCreatedType getType() {
        return type;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FileCreatedFile file) {
        this.file = file;
    }
    public FileCreatedFile getFile() {
        return file;
    }
}