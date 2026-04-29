package example.testGenerate.test.common;

import javax.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2;

@Generated("Yojo")
public class DeepNestingSchemaLevel1 {

    @Valid
    private DeepNestingSchemaLevel1Level2 level2;

    public void setLevel2(DeepNestingSchemaLevel1Level2 level2) {
        this.level2 = level2;
    }
    public DeepNestingSchemaLevel1Level2 getLevel2() {
        return level2;
    }
}