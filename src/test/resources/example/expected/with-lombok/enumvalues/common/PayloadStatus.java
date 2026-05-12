package enumvalues.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Accessors(fluent = true, chain = true)
public enum PayloadStatus {
    ACTIVE("A"),
    INACTIVE("I");

    private final String value;

    PayloadStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PayloadStatus fromValue(String value) {
        for (PayloadStatus v : PayloadStatus.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}