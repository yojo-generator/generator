package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class CustomSchema {


    private Long baseField;

    private String specificField;
    public void setBaseField(Long baseField) {
        this.baseField = baseField;
    }
    public Long getBaseField() {
        return baseField;
    }
    public void setSpecificField(String specificField) {
        this.specificField = specificField;
    }
    public String getSpecificField() {
        return specificField;
    }
    @Override
    public String toString() {
        return "CustomSchema{" +
                "baseField=" + baseField + ", " +
                "specificField=" + specificField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomSchema that = (CustomSchema) o;
        return Objects.equals(baseField, that.baseField) &&
                Objects.equals(specificField, that.specificField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseField, specificField);
    }
}