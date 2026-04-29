package example.testGenerate.test.common;

import testGenerate.ClassForExtending;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class CollectionTypesInnerSchema {

    private String someString;

    @Valid
    private ClassForExtending someExistingObject;

}