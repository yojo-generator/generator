package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class PolymorphExampleFour {


    private Boolean oneMoreField;

    private String status;

    private Integer someField;
    public void setOneMoreField(Boolean oneMoreField) {
        this.oneMoreField = oneMoreField;
    }
    public Boolean getOneMoreField() {
        return oneMoreField;
    }
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
    @Override
    public String toString() {
        return "PolymorphExampleFour{" +
                "oneMoreField=" + oneMoreField + ", " +
                "status=" + status + ", " +
                "someField=" + someField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolymorphExampleFour that = (PolymorphExampleFour) o;
        return Objects.equals(oneMoreField, that.oneMoreField) &&
                Objects.equals(status, that.status) &&
                Objects.equals(someField, that.someField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oneMoreField, status, someField);
    }
}