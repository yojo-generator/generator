package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ImCreatedChannel;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.ImCreatedType;

/**
* A DM was created.
*/
@Generated("Yojo")
public class ImCreated {

    private ImCreatedType type;

    @Valid
    private ImCreatedChannel channel;

    private String user;

    public void setChannel(ImCreatedChannel channel) {
        this.channel = channel;
    }
    public ImCreatedChannel getChannel() {
        return channel;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
}