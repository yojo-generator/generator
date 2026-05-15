package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

/**
* Inheritance object
*/
@Generated("Yojo")
public class RequestDtoWithPropertiesSomeOneObject {


    /**
     * someField description
     */
    private String someField;
    public void setSomeField(String someField) {
        this.someField = someField;
    }
    public String getSomeField() {
        return someField;
    }
    @Override
    public String toString() {
        return "RequestDtoWithPropertiesSomeOneObject{" +
                "someField=" + someField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDtoWithPropertiesSomeOneObject that = (RequestDtoWithPropertiesSomeOneObject) o;
        return Objects.equals(someField, that.someField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someField);
    }
}