package example.testGenerate.test.common;

import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DeepNestingSchemaLevel1 {

    @Valid
    private DeepNestingSchemaLevel1Level2 level2;

}