package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.GroupLeftType;
import javax.annotation.processing.Generated;

/**
* You left a private channel.
*/
@Generated("Yojo")
public class GroupLeft {

    private GroupLeftType type;

    private String channel;

    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getChannel() {
        return channel;
    }
}