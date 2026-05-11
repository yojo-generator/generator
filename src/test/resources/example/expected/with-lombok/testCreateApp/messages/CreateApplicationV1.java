package testCreateApp.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import testCreateApp.common.CreateApplicationV1RequestData;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CreateApplicationV1 {


    @Valid
    private CreateApplicationV1RequestData requestData;
}