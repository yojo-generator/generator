package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;

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
    @Override
    public String toString() {
        return "DeepNestingSchemaLevel1{" +
                "level2=" + level2 +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeepNestingSchemaLevel1 that = (DeepNestingSchemaLevel1) o;
        return Objects.equals(level2, that.level2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level2);
    }
}