package example.testGenerate.test.messages;

import example.testGenerate.test.common.RequestDtoWithPropertiesAComplexProperty;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import example.testGenerate.test.common.RequestDtoWithPropertiesSomeOneObject;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class RequestDtoWithProperties {

    private String someString;

    /**
     * Inheritance object
     */
    @Valid
    private RequestDtoWithPropertiesSomeOneObject someOneObject;

    @Valid
    private RequestDtoWithPropertiesAComplexProperty aComplexProperty;

}