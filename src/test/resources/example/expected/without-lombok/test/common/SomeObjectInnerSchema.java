package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import testGenerate.ExistingClass;

@Generated("Yojo")
public class SomeObjectInnerSchema {


    private String someString;

    @Valid
    private ExistingClass someExistingObject;
    public void setSomeString(String someString) {
        this.someString = someString;
    }
    public String getSomeString() {
        return someString;
    }
    public void setSomeExistingObject(ExistingClass someExistingObject) {
        this.someExistingObject = someExistingObject;
    }
    public ExistingClass getSomeExistingObject() {
        return someExistingObject;
    }
    @Override
    public String toString() {
        return "SomeObjectInnerSchema{" +
                "someString=" + someString + ", " +
                "someExistingObject=" + someExistingObject +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeObjectInnerSchema that = (SomeObjectInnerSchema) o;
        return Objects.equals(someString, that.someString) &&
                Objects.equals(someExistingObject, that.someExistingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someString, someExistingObject);
    }
}