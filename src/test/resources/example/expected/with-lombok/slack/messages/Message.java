package slack.messages;

import java.util.List;
import slack.common.MessageEdited;
import slack.common.MessageType;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import slack.common.Attachment;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A message was sent to a channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class Message {

    private MessageType type;

    private String user;

    private String channel;

    private String text;

    private String ts;

    @Valid
    private List<Attachment> attachments;

    @Valid
    private MessageEdited edited;

}