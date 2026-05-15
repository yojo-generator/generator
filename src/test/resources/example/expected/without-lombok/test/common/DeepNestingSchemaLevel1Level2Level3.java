package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class DeepNestingSchemaLevel1Level2Level3 {


    private String value;
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "DeepNestingSchemaLevel1Level2Level3{" +
                "value=" + value +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeepNestingSchemaLevel1Level2Level3 that = (DeepNestingSchemaLevel1Level2Level3) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}