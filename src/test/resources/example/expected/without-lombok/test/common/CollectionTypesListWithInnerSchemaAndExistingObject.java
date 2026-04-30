package example.testGenerate.test.common;

import testGenerate.ClassForExtending;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class CollectionTypesListWithInnerSchemaAndExistingObject {

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