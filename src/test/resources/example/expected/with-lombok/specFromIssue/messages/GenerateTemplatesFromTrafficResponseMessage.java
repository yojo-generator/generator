package specFromIssue.messages;

import java.util.List;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import specFromIssue.common.StatusSchema;

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