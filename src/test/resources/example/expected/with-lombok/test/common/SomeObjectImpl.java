package example.testGenerate.test.common;

import testGenerate.InterfaceForImpl;
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
public class SomeObjectImpl implements InterfaceForImpl {

    private Integer someInteger;

}