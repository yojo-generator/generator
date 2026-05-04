package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.FileDeletedType;

@Generated("Yojo")
/**
* A file was deleted.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class FileDeleted {


    private FileDeletedType type;

    private String fileId;

    private String eventTs;
}