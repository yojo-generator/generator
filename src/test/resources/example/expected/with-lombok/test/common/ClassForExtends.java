package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* This Class just fo Example of extends after generate
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class ClassForExtends {


    private String someAnotherString;
}