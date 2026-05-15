package example.testGenerate.test.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

/**
* This Class just fo Example of extends after generate
*/
@Generated("Yojo")
public class ClassForExtends {


    private String someAnotherString;
    public void setSomeAnotherString(String someAnotherString) {
        this.someAnotherString = someAnotherString;
    }
    public String getSomeAnotherString() {
        return someAnotherString;
    }
    @Override
    public String toString() {
        return "ClassForExtends{" +
                "someAnotherString=" + someAnotherString +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassForExtends that = (ClassForExtends) o;
        return Objects.equals(someAnotherString, that.someAnotherString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(someAnotherString);
    }
}