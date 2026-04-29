package example.testGenerate.specFromIssue.messages;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import example.testGenerate.specFromIssue.common.StatusSchema;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* ответ с payload = array of objects (items только с examples)
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class GenerateTemplatesFromTrafficResponseMessage {

    @NotEmpty
    private List<Object> payload;

    @Valid
    @NotNull
    private StatusSchema status;

}