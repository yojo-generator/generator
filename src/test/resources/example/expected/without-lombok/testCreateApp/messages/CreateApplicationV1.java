package example.testGenerate.testCreateApp.messages;

import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestData;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class CreateApplicationV1 {

    @Valid
    private CreateApplicationV1RequestData requestData;

    public void setRequestData(CreateApplicationV1RequestData requestData) {
        this.requestData = requestData;
    }
    public CreateApplicationV1RequestData getRequestData() {
        return requestData;
    }
}