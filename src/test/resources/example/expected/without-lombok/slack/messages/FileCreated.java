package slack.messages;

import javax.validation.Valid;
import slack.common.FileCreatedType;
import javax.annotation.processing.Generated;
import slack.common.FileCreatedFile;

/**
* A file was created.
*/
@Generated("Yojo")
public class FileCreated {

    private FileCreatedType type;

    private String fileId;

    @Valid
    private FileCreatedFile file;

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