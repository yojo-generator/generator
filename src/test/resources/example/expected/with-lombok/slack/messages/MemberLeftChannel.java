package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.MemberLeftChannelType;
import example.testGenerate.slack.common.MemberLeftChannelChannelType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* A user left a public or private channel.
*/
@Generated("Yojo")
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