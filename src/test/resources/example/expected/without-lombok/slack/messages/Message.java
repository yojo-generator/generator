package example.testGenerate.slack.messages;

import java.util.List;
import example.testGenerate.slack.common.Attachment;
import example.testGenerate.slack.common.MessageEdited;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.MessageType;

/**
* A message was sent to a channel.
*/
@Generated("Yojo")
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

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getTs() {
        return ts;
    }
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
    public List<Attachment> getAttachments() {
        return attachments;
    }
    public void setEdited(MessageEdited edited) {
        this.edited = edited;
    }
    public MessageEdited getEdited() {
        return edited;
    }
}