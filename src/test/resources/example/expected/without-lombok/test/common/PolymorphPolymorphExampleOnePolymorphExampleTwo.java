package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class PolymorphPolymorphExampleOnePolymorphExampleTwo {


    private Integer someField;

    private String status;
    public void setSomeField(Integer someField) {
        this.someField = someField;
    }
    public Integer getSomeField() {
        return someField;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return "PolymorphPolymorphExampleOnePolymorphExampleTwo{" +
                "someField=" + someField + ", " +
                "status=" + status +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolymorphPolymorphExampleOnePolymorphExampleTwo that = (PolymorphPolymorphExampleOnePolymorphExampleTwo) o;
        return Objects.equals(someField, that.someField) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someField, status);
    }
}