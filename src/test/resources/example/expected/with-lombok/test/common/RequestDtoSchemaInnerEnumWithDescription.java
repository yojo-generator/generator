package example.testGenerate.test.common;

import lombok.Getter;
import javax.annotation.processing.Generated;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Getter
@Accessors(fluent = true, chain = true)
public enum RequestDtoSchemaInnerEnumWithDescription {
    SUCCESS("Success value"),
    DECLINE("Decline value"),
    ERROR("Error value");

    private final String value;

    RequestDtoSchemaInnerEnumWithDescription(String value) {
        this.value = value;
    }

}