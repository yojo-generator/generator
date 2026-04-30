package example.testGenerate.test.common;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

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