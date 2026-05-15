package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ExampleFiveInnerSchema {


    private String someString;
    public void setSomeString(String someString) {
        this.someString = someString;
    }
    public String getSomeString() {
        return someString;
    }
    @Override
    public String toString() {
        return "ExampleFiveInnerSchema{" +
                "someString=" + someString +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleFiveInnerSchema that = (ExampleFiveInnerSchema) o;
        return Objects.equals(someString, that.someString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someString);
    }
}