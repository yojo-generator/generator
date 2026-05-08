package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.MemberLeftChannelChannelType;
import slack.common.MemberLeftChannelType;

@Generated("Yojo")
/**
* A user left a public or private channel.
*/
public class MemberLeftChannel {


    private MemberLeftChannelType type;

    private String user;

    private String channel;

    private MemberLeftChannelChannelType channelType;

    private String team;
    public void setType(MemberLeftChannelType type) {
        this.type = type;
    }
    public MemberLeftChannelType getType() {
        return type;
    }
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
    public void setChannelType(MemberLeftChannelChannelType channelType) {
        this.channelType = channelType;
    }
    public MemberLeftChannelChannelType getChannelType() {
        return channelType;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public String getTeam() {
        return team;
    }
}