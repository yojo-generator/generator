package slack.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class MessageEdited {

    private String user;

    private String ts;

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getTs() {
        return ts;
    }
}