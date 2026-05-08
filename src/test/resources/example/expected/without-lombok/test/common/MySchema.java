package example.testGenerate.test.common;

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
}