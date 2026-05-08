package testCreateApp.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import testCreateApp.common.CreateApplicationV1RequestDataConditions;

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