package testCreateApp.messages;

import javax.validation.Valid;
import testCreateApp.common.CreateApplicationV1RequestData;
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