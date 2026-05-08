package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.MemberJoinedChannelChannelType;
import slack.common.MemberJoinedChannelType;

@Generated("Yojo")
/**
* A user joined a public or private channel.
*/
public class MemberJoinedChannel {


    private MemberJoinedChannelType type;

    private String user;

    private String channel;

    private MemberJoinedChannelChannelType channelType;

    private String team;

    private String inviter;
    public void setType(MemberJoinedChannelType type) {
        this.type = type;
    }
    public MemberJoinedChannelType getType() {
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
    public void setChannelType(MemberJoinedChannelChannelType channelType) {
        this.channelType = channelType;
    }
    public MemberJoinedChannelChannelType getChannelType() {
        return channelType;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public String getTeam() {
        return team;
    }
    public void setInviter(String inviter) {
        this.inviter = inviter;
    }
    public String getInviter() {
        return inviter;
    }
}