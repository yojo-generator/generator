package example.testGenerate.testCreateApp.common;

import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestDataConditions;
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