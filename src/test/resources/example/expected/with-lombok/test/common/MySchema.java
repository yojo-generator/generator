package example.testGenerate.test.common;

import lombok.NonNull;
import lombok.Builder;
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
@Builder
public class MySchema {

    @NonNull
    private String myField;

}