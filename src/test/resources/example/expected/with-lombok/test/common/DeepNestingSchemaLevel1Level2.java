package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2Level3;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class DeepNestingSchemaLevel1Level2 {


    @Valid
    private DeepNestingSchemaLevel1Level2Level3 level3;
}