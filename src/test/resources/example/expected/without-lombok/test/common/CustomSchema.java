package example.testGenerate.test.common;

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
}