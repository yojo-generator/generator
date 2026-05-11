package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.EmailDomainChangedType;

/**
* The workspace email domain has changed.
*/
@Generated("Yojo")
public class EmailDomainChanged {


    private EmailDomainChangedType type;

    private String emailDomain;

    private String eventTs;
    public void setType(EmailDomainChangedType type) {
        this.type = type;
    }
    public EmailDomainChangedType getType() {
        return type;
    }
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