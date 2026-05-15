package example.testGenerate.test.common;

import example.testGenerate.test.common.DeepNestingSchemaLevel1;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;

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
    @Override
    public String toString() {
        return "DeepNestingSchema{" +
                "level1=" + level1 +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeepNestingSchema that = (DeepNestingSchema) o;
        return Objects.equals(level1, that.level1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level1);
    }
}