package slack.messages;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.EmailDomainChangedType;

/**
* The workspace email domain has changed.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class EmailDomainChanged {


    private EmailDomainChangedType type;

    private String emailDomain;

    private String eventTs;
}