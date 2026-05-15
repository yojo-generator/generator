package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1Level2Level3;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;

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
    @Override
    public String toString() {
        return "DeepNestingSchemaLevel1Level2{" +
                "level3=" + level3 +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeepNestingSchemaLevel1Level2 that = (DeepNestingSchemaLevel1Level2) o;
        return Objects.equals(level3, that.level3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level3);
    }
}