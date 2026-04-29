package example.testGenerate.slack.messages;

import java.util.List;
import example.testGenerate.slack.common.Attachment;
import example.testGenerate.slack.common.MessageEdited;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import example.testGenerate.slack.common.MessageType;
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