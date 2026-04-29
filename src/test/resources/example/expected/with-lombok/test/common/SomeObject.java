package example.testGenerate.test.common;

import example.testGenerate.test.common.SomeObjectInnerSchema;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.test.common.ClassForExtends;

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
}