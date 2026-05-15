package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class CommonBase {


    private Long baseField;
    public void setBaseField(Long baseField) {
        this.baseField = baseField;
    }
    public Long getBaseField() {
        return baseField;
    }
    @Override
    public String toString() {
        return "CommonBase{" +
                "baseField=" + baseField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonBase that = (CommonBase) o;
        return Objects.equals(baseField, that.baseField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseField);
    }
}