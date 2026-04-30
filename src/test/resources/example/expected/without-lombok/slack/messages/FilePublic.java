package slack.messages;

import slack.common.FilePublicType;
import javax.validation.Valid;
import slack.common.FilePublicFile;
import javax.annotation.processing.Generated;

/**
* A file was made public.
*/
@Generated("Yojo")
public class FilePublic {

    private FilePublicType type;

    private String fileId;

    @Valid
    private FilePublicFile file;

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