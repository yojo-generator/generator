package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public enum EnumResultWithDescription {
    SUCCESS("Success value"),
    DECLINE("Decline value"),
    ERROR("Error value");

    private final String value;

    EnumResultWithDescription(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}