package specFromIssue.messages;

import java.util.List;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import specFromIssue.common.StatusSchema;

@Generated("Yojo")
/**
* ответ с payload = array of objects (items только с examples)
*/
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