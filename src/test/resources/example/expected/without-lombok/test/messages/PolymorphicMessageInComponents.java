package example.testGenerate.test.messages;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public class PolymorphicMessageInComponents {

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