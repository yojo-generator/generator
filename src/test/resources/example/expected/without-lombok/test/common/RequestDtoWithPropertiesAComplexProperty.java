package example.testGenerate.test.common;

import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class RequestDtoWithPropertiesAComplexProperty {

    /**
     * A dynamic object where each key is a feature, and the value
     * Example: {prop1=100, prop2=foo, prop3=true}
     */
    private Object prop1;

    public void setProp1(Object prop1) {
        this.prop1 = prop1;
    }
    public Object getProp1() {
        return prop1;
    }
}