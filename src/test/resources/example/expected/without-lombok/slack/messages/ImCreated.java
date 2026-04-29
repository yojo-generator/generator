package slack.messages;

import slack.common.ImCreatedType;
import javax.validation.Valid;
import slack.common.ImCreatedChannel;
import javax.annotation.processing.Generated;

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