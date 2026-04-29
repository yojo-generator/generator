package specFromIssue.messages;

import java.util.List;
import specFromIssue.common.StatusSchema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
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