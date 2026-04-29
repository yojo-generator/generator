package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FilePublicFile;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.FilePublicType;

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