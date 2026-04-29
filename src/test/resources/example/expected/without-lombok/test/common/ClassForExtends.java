package example.testGenerate.test.common;

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
}