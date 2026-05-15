package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.NonNull;

@Generated("Yojo")
@Builder
public class MySchema {


    @NonNull
    private String myField;
    public void setMyField(String myField) {
        this.myField = myField;
    }
    public String getMyField() {
        return myField;
    }
    @Override
    public String toString() {
        return "MySchema{" +
                "myField=" + myField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MySchema that = (MySchema) o;
        return Objects.equals(myField, that.myField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myField);
    }
}