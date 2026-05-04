package example.testGenerate.test.messages;

import example.testGenerate.test.common.RequestDtoWithPropertiesAComplexProperty;
import example.testGenerate.test.common.RequestDtoWithPropertiesSomeOneObject;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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