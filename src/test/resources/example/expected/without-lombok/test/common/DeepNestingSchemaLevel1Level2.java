package example.testGenerate.test.common;

import javax.validation.Valid;
import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2Level3;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class DeepNestingSchemaLevel1Level2 {

    @Valid
    private DeepNestingSchemaLevel1Level2Level3 level3;

    public void setLevel3(DeepNestingSchemaLevel1Level2Level3 level3) {
        this.level3 = level3;
    }
    public DeepNestingSchemaLevel1Level2Level3 getLevel3() {
        return level3;
    }
}