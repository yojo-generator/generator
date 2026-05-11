package slack.common;

import java.math.BigDecimal;
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
}