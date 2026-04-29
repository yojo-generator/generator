package example.testGenerate.test.messages;

import example.testGenerate.test.common.RequestDtoWithPropertiesAComplexProperty;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.test.common.RequestDtoWithPropertiesSomeOneObject;

@Generated("Yojo")
public class RequestDtoWithProperties {

    private String someString;

    /**
     * Inheritance object
     */
    @Valid
    private RequestDtoWithPropertiesSomeOneObject someOneObject;

    @Valid
    private RequestDtoWithPropertiesAComplexProperty aComplexProperty;

    public void setSomeString(String someString) {
        this.someString = someString;
    }
    public String getSomeString() {
        return someString;
    }
    public void setSomeOneObject(RequestDtoWithPropertiesSomeOneObject someOneObject) {
        this.someOneObject = someOneObject;
    }
    public RequestDtoWithPropertiesSomeOneObject getSomeOneObject() {
        return someOneObject;
    }
    public void setAComplexProperty(RequestDtoWithPropertiesAComplexProperty aComplexProperty) {
        this.aComplexProperty = aComplexProperty;
    }
    public RequestDtoWithPropertiesAComplexProperty getAComplexProperty() {
        return aComplexProperty;
    }
}