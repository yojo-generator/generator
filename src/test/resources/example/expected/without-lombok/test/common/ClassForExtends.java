package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
/**
* This Class just fo Example of extends after generate
*/
public class ClassForExtends {


    private String someAnotherString;
    public void setSomeAnotherString(String someAnotherString) {
        this.someAnotherString = someAnotherString;
    }
    public String getSomeAnotherString() {
        return someAnotherString;
    }
}