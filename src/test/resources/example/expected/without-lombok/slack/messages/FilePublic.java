package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.FilePublicFile;
import slack.common.FilePublicType;

/**
* A file was made public.
*/
@Generated("Yojo")
public class FilePublic {


    private FilePublicType type;

    private String fileId;

    @Valid
    private FilePublicFile file;
    public void setType(FilePublicType type) {
        this.type = type;
    }
    public FilePublicType getType() {
        return type;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFile(FilePublicFile file) {
        this.file = file;
    }
    public FilePublicFile getFile() {
        return file;
    }
}