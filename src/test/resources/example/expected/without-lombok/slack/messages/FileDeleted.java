package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.FileDeletedType;

/**
* A file was deleted.
*/
@Generated("Yojo")
public class FileDeleted {


    private FileDeletedType type;

    private String fileId;

    private String eventTs;
    public void setType(FileDeletedType type) {
        this.type = type;
    }
    public FileDeletedType getType() {
        return type;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}