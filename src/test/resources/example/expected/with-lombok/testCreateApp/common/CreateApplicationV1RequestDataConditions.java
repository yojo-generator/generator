package example.testGenerate.testCreateApp.common;

import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestDataConditionsRequested;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CreateApplicationV1RequestDataConditions {

    @Valid
    private CreateApplicationV1RequestDataConditionsRequested requested;

}