package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class DeepNestingSchema {

    @Valid
    private DeepNestingSchemaLevel1 level1;

    public void setLevel1(DeepNestingSchemaLevel1 level1) {
        this.level1 = level1;
    }
    public DeepNestingSchemaLevel1 getLevel1() {
        return level1;
    }
}