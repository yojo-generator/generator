package example.testGenerate.test.common;

import testGenerate.InterfaceForImpl;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class SomeObjectImpl implements InterfaceForImpl {

    private Integer someInteger;

    public void setSomeInteger(Integer someInteger) {
        this.someInteger = someInteger;
    }
    public Integer getSomeInteger() {
        return someInteger;
    }
}