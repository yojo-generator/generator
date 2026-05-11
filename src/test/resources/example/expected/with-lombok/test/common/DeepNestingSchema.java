package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1;
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
public class DeepNestingSchema {


    @Valid
    private DeepNestingSchemaLevel1 level1;
}