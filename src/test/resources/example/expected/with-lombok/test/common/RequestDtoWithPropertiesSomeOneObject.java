package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
* Inheritance object
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestDtoWithPropertiesSomeOneObject {


    /**
     * someField description
     */
    private String someField;
}