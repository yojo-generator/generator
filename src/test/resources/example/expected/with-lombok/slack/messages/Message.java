package slack.messages;

import java.util.List;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.Attachment;
import slack.common.MessageEdited;
import slack.common.MessageType;

@Generated("Yojo")
/**
* A message was sent to a channel.
*/
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