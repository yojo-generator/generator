package enumvalues.common;

import enumvalues.common.PayloadStatus;
import enumvalues.common.PayloadStatusWithDefault;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Payload {


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