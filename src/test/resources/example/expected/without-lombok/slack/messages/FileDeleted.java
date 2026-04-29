package slack.messages;

import slack.common.FileDeletedType;
import javax.annotation.processing.Generated;

/**
* A file was deleted.
*/
@Generated("Yojo")
public class FileDeleted {

    private FileDeletedType type;

    private String fileId;

    private String eventTs;

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