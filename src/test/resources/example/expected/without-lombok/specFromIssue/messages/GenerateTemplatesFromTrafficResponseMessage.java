package example.testGenerate.specFromIssue.messages;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import example.testGenerate.specFromIssue.common.StatusSchema;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import jakarta.validation.constraints.NotEmpty;

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