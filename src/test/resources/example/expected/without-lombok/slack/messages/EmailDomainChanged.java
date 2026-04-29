package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.EmailDomainChangedType;
import javax.annotation.processing.Generated;

/**
* The workspace email domain has changed.
*/
@Generated("Yojo")
public class EmailDomainChanged {

    private EmailDomainChangedType type;

    private String emailDomain;

    private String eventTs;

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }
    public String getEmailDomain() {
        return emailDomain;
    }
    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }
    public String getEventTs() {
        return eventTs;
    }
}