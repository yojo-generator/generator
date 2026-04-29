package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.MemberJoinedChannelType;
import example.testGenerate.slack.common.MemberJoinedChannelChannelType;
import javax.annotation.processing.Generated;

/**
* A user joined a public or private channel.
*/
@Generated("Yojo")
public class MemberJoinedChannel {

    private MemberJoinedChannelType type;

    private String user;

    private String channel;

    private MemberJoinedChannelChannelType channelType;

    private String team;

    private String inviter;

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
    public void setInviter(String inviter) {
        this.inviter = inviter;
    }
    public String getInviter() {
        return inviter;
    }
}