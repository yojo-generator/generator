package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class PolymorphExampleThree {

    private String status;

    private Integer someField;

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setSomeField(Integer someField) {
        this.someField = someField;
    }
    public Integer getSomeField() {
        return someField;
    }
}