package slack.common;

import java.util.Objects;
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
    @Override
    public String toString() {
        return "MessageEdited{" +
                "user=" + user + ", " +
                "ts=" + ts +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEdited that = (MessageEdited) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, ts);
    }
}