package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ExampleFour {


    private Boolean fromFour;

    private Boolean oneMoreField;

    private String fromFive;
    public void setFromFour(Boolean fromFour) {
        this.fromFour = fromFour;
    }
    public Boolean getFromFour() {
        return fromFour;
    }
    public void setOneMoreField(Boolean oneMoreField) {
        this.oneMoreField = oneMoreField;
    }
    public Boolean getOneMoreField() {
        return oneMoreField;
    }
    public void setFromFive(String fromFive) {
        this.fromFive = fromFive;
    }
    public String getFromFive() {
        return fromFive;
    }
    @Override
    public String toString() {
        return "ExampleFour{" +
                "fromFour=" + fromFour + ", " +
                "oneMoreField=" + oneMoreField + ", " +
                "fromFive=" + fromFive +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleFour that = (ExampleFour) o;
        return Objects.equals(fromFour, that.fromFour) &&
                Objects.equals(oneMoreField, that.oneMoreField) &&
                Objects.equals(fromFive, that.fromFive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromFour, oneMoreField, fromFive);
    }
}