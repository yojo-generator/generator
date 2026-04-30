package testCreateApp.common;

import testCreateApp.common.CreateApplicationV1RequestDataConditionsRequested;
import javax.validation.Valid;
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
public class CreateApplicationV1RequestDataConditions {

    @Valid
    private CreateApplicationV1RequestDataConditionsRequested requested;

}