package example.testGenerate.testCreateApp.common;

import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestDataConditionsRequested;

@Generated("Yojo")
public class CreateApplicationV1RequestDataConditions {

    @Valid
    private CreateApplicationV1RequestDataConditionsRequested requested;

    public void setRequested(CreateApplicationV1RequestDataConditionsRequested requested) {
        this.requested = requested;
    }
    public CreateApplicationV1RequestDataConditionsRequested getRequested() {
        return requested;
    }
}