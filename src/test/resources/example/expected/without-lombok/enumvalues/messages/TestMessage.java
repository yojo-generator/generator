package enumvalues.messages;

import enumvalues.common.PayloadStatus;
import enumvalues.common.PayloadStatusWithDefault;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class TestMessage {


    private PayloadStatus status;

    private PayloadStatusWithDefault statusWithDefault;
    public void setStatus(PayloadStatus status) {
        this.status = status;
    }
    public PayloadStatus getStatus() {
        return status;
    }
    public void setStatusWithDefault(PayloadStatusWithDefault statusWithDefault) {
        this.statusWithDefault = statusWithDefault;
    }
    public PayloadStatusWithDefault getStatusWithDefault() {
        return statusWithDefault;
    }
}