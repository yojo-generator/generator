package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import lombok.Getter;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Getter
@Accessors(fluent = true, chain = true)
public enum ExampleFiveInnerEnumWithDescription {
    SUCCESS("Success value"),
    DECLINE("Decline value"),
    ERROR("Error value");

    private final String value;

    ExampleFiveInnerEnumWithDescription(String value) {
        this.value = value;
    }

}