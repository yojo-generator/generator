package enumvalues.common;

import enumvalues.common.ErrorCode;
import enumvalues.common.PayloadStatus;
import enumvalues.common.PayloadStatusWithDefault;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class Payload {


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
    @Override
    public String toString() {
        return "Payload{" +
                "status=" + status + ", " +
                "statusWithDefault=" + statusWithDefault + ", " +
                "error=" + error +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload that = (Payload) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(statusWithDefault, that.statusWithDefault) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, statusWithDefault, error);
    }
}