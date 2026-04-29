package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1;
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
public class DeepNestingSchema {

    @Valid
    private DeepNestingSchemaLevel1 level1;

}