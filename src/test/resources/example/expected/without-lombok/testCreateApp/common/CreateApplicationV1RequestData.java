package testCreateApp.common;

import testCreateApp.common.CreateApplicationV1RequestDataConditions;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class CreateApplicationV1RequestData {

    @Valid
    private CreateApplicationV1RequestDataConditions conditions;

    public void setConditions(CreateApplicationV1RequestDataConditions conditions) {
        this.conditions = conditions;
    }
    public CreateApplicationV1RequestDataConditions getConditions() {
        return conditions;
    }
}