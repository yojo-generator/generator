package example.testGenerate.testCreateApp.messages;

import example.testGenerate.testCreateApp.common.CreateApplicationV1RequestData;
import lombok.Data;
import jakarta.validation.Valid;
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