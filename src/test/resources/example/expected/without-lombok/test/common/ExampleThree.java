package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ExampleThree {


    private Boolean oneMoreField;

    private String status;

    private Integer someField;

    private Boolean fromFour;
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
    public void setFromFour(Boolean fromFour) {
        this.fromFour = fromFour;
    }
    public Boolean getFromFour() {
        return fromFour;
    }
    @Override
    public String toString() {
        return "ExampleThree{" +
                "oneMoreField=" + oneMoreField + ", " +
                "status=" + status + ", " +
                "someField=" + someField + ", " +
                "fromFour=" + fromFour +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleThree that = (ExampleThree) o;
        return Objects.equals(oneMoreField, that.oneMoreField) &&
                Objects.equals(status, that.status) &&
                Objects.equals(someField, that.someField) &&
                Objects.equals(fromFour, that.fromFour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oneMoreField, status, someField, fromFour);
    }
}