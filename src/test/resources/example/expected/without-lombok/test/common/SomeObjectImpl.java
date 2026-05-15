package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import testGenerate.InterfaceForImpl;

@Generated("Yojo")
public class SomeObjectImpl implements InterfaceForImpl {


    private Integer someInteger;
    public void setSomeInteger(Integer someInteger) {
        this.someInteger = someInteger;
    }
    public Integer getSomeInteger() {
        return someInteger;
    }
    @Override
    public String toString() {
        return "SomeObjectImpl{" +
                "someInteger=" + someInteger +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeObjectImpl that = (SomeObjectImpl) o;
        return Objects.equals(someInteger, that.someInteger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someInteger);
    }
}