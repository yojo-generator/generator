package slack.messages;

import slack.common.MemberLeftChannelChannelType;
import slack.common.MemberLeftChannelType;
import javax.annotation.processing.Generated;

/**
* A user left a public or private channel.
*/
@Generated("Yojo")
public class MemberLeftChannel {

    private MemberLeftChannelType type;

    private String user;

    private String channel;

    private MemberLeftChannelChannelType channelType;

    private String team;

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
    public void setTeam(String team) {
        this.team = team;
    }
    public String getTeam() {
        return team;
    }
}