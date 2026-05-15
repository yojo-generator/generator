package example.testGenerate.test.common;

import example.testGenerate.test.common.ClassForExtends;
import example.testGenerate.test.common.SomeObjectInnerSchema;
import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;

@Generated("Yojo")
public class SomeObject extends ClassForExtends {


    private Integer someInteger;

    @Valid
    private SomeObjectInnerSchema innerSchema;
    public void setSomeInteger(Integer someInteger) {
        this.someInteger = someInteger;
    }
    public Integer getSomeInteger() {
        return someInteger;
    }
    public void setInnerSchema(SomeObjectInnerSchema innerSchema) {
        this.innerSchema = innerSchema;
    }
    public SomeObjectInnerSchema getInnerSchema() {
        return innerSchema;
    }
    @Override
    public String toString() {
        return "SomeObject{" +
                "someInteger=" + someInteger + ", " +
                "innerSchema=" + innerSchema +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeObject that = (SomeObject) o;
        return Objects.equals(someInteger, that.someInteger) &&
                Objects.equals(innerSchema, that.innerSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someInteger, innerSchema);
    }
}