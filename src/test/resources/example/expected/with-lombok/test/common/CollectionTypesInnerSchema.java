package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import testGenerate.ClassForExtending;

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