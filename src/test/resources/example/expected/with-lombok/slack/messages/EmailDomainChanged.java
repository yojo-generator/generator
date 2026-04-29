package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.EmailDomainChangedType;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

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