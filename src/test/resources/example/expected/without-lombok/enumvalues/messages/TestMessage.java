package enumvalues.messages;

import enumvalues.common.ErrorCode;
import enumvalues.common.PayloadStatus;
import enumvalues.common.PayloadStatusWithDefault;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class TestMessage {


    private PayloadStatus status;

    private PayloadStatusWithDefault statusWithDefault;

    private ErrorCode error;
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
    public void setError(ErrorCode error) {
        this.error = error;
    }
    public ErrorCode getError() {
        return error;
    }
}