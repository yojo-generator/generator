package slack.messages;

import slack.common.MemberJoinedChannelChannelType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import slack.common.MemberJoinedChannelType;

/**
* A user joined a public or private channel.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class MemberJoinedChannel {

    private MemberJoinedChannelType type;

    private String user;

    private String channel;

    private MemberJoinedChannelChannelType channelType;

    private String team;

    private String inviter;

}