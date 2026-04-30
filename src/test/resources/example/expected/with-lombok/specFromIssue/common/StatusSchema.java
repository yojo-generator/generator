package specFromIssue.common;

import javax.validation.constraints.NotBlank;
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
public class StatusSchema {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

}