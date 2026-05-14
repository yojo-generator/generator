package example.testGenerate.test.common;

import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated("Yojo")
@Getter
public enum EnumResultWithDescription {
    SUCCESS("Success value"),
    DECLINE("Decline value"),
    ERROR("Error value");

    private final String value;

    EnumResultWithDescription(String value) {
        this.value = value;
    }

}