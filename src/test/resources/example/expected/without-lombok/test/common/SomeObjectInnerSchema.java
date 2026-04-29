package example.testGenerate.test.common;

import testGenerate.ExistingClass;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;

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
}