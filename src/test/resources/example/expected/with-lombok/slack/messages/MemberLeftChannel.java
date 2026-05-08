package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.MemberLeftChannelChannelType;
import slack.common.MemberLeftChannelType;

@Generated("Yojo")
/**
* A user left a public or private channel.
*/
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class MemberLeftChannel {


    private MemberLeftChannelType type;

    private String user;

    private String channel;

    private MemberLeftChannelChannelType channelType;

    private String team;
}