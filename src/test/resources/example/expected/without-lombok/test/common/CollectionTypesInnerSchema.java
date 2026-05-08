package example.testGenerate.test.common;

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
}