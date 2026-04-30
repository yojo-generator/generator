package testCreateApp.messages;

import javax.validation.Valid;
import testCreateApp.common.CreateApplicationV1RequestData;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CreateApplicationV1 {

    @Valid
    private CreateApplicationV1RequestData requestData;

}