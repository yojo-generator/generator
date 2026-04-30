package testCreateApp.common;

import testCreateApp.common.CreateApplicationV1RequestDataConditions;
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
public class CreateApplicationV1RequestData {

    @Valid
    private CreateApplicationV1RequestDataConditions conditions;

}