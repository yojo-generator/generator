package gitter.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ChatMessageIssues {


    private String number;
    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }
    @Override
    public String toString() {
        return "ChatMessageIssues{" +
                "number=" + number +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageIssues that = (ChatMessageIssues) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}