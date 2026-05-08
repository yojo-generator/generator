package testCreateApp.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import testCreateApp.common.CreateApplicationV1RequestDataConditions;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CreateApplicationV1RequestData {


    @Valid
    private CreateApplicationV1RequestDataConditions conditions;
}