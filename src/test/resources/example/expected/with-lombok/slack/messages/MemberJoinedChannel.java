package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.MemberJoinedChannelChannelType;
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