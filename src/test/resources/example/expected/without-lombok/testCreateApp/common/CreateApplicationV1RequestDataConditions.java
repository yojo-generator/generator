package testCreateApp.common;

import testCreateApp.common.CreateApplicationV1RequestDataConditionsRequested;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

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