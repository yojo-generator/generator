package example.testGenerate.test.common;

import lombok.NonNull;
import lombok.Builder;
import javax.annotation.processing.Generated;

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