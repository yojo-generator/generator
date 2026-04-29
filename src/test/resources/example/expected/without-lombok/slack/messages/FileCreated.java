package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.FileCreatedFile;
import example.testGenerate.slack.common.FileCreatedType;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

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