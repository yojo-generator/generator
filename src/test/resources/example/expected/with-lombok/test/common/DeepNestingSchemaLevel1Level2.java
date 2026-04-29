package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2Level3;
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
public class DeepNestingSchemaLevel1Level2 {

    @Valid
    private DeepNestingSchemaLevel1Level2Level3 level3;

}