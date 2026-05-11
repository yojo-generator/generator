package example.testGenerate.test.common;

import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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