package example.testGenerate.test.common;

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
}