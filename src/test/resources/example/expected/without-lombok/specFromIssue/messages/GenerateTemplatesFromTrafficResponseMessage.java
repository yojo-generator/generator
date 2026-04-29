package specFromIssue.messages;

import java.util.List;
import specFromIssue.common.StatusSchema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

/**
* ответ с payload = array of objects (items только с examples)
*/
@Generated("Yojo")
public class GenerateTemplatesFromTrafficResponseMessage {

    @NotEmpty
    private List<Object> payload;

    @Valid
    @NotNull
    private StatusSchema status;

    public void setPayload(List<Object> payload) {
        this.payload = payload;
    }
    public List<Object> getPayload() {
        return payload;
    }
    public void setStatus(StatusSchema status) {
        this.status = status;
    }
    public StatusSchema getStatus() {
        return status;
    }
}