package specFromIssue.common;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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