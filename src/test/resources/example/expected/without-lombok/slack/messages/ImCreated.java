package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.ImCreatedChannel;
import slack.common.ImCreatedType;

@Generated("Yojo")
/**
* A DM was created.
*/
public class ImCreated {


    private ImCreatedType type;

    @Valid
    private ImCreatedChannel channel;

    private String user;
    public void setType(ImCreatedType type) {
        this.type = type;
    }
    public ImCreatedType getType() {
        return type;
    }
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