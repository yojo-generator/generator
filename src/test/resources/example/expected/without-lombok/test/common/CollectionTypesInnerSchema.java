package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import testGenerate.ClassForExtending;

@Generated("Yojo")
public class CollectionTypesInnerSchema {


    private String someString;

    @Valid
    private ClassForExtending someExistingObject;
    public void setSomeString(String someString) {
        this.someString = someString;
    }
    public String getSomeString() {
        return someString;
    }
    public void setSomeExistingObject(ClassForExtending someExistingObject) {
        this.someExistingObject = someExistingObject;
    }
    public ClassForExtending getSomeExistingObject() {
        return someExistingObject;
    }
    @Override
    public String toString() {
        return "CollectionTypesInnerSchema{" +
                "someString=" + someString + ", " +
                "someExistingObject=" + someExistingObject +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionTypesInnerSchema that = (CollectionTypesInnerSchema) o;
        return Objects.equals(someString, that.someString) &&
                Objects.equals(someExistingObject, that.someExistingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someString, someExistingObject);
    }
}