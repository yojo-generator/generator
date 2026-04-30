package example.testGenerate.test.common;

import java.util.Map;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestDtoWithPropertiesAComplexProperty {

    /**
     * A dynamic object where each key is a feature, and the value
     * Example: {prop1=100, prop2=foo, prop3=true}
     */
    private Object prop1;

}