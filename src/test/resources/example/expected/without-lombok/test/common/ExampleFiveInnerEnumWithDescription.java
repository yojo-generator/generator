package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public enum ExampleFiveInnerEnumWithDescription {
    SUCCESS("Success value"),
    DECLINE("Decline value"),
    ERROR("Error value");

    private final String value;

    ExampleFiveInnerEnumWithDescription(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}