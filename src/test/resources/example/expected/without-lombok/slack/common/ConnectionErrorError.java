package slack.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ConnectionErrorError {


    private BigDecimal code;

    private String msg;
    public void setCode(BigDecimal code) {
        this.code = code;
    }
    public BigDecimal getCode() {
        return code;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
    @Override
    public String toString() {
        return "ConnectionErrorError{" +
                "code=" + code + ", " +
                "msg=" + msg +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionErrorError that = (ConnectionErrorError) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg);
    }
}