package example.testGenerate.testCreateApp.common;

import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestDataConditions;

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