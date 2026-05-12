package enumvalues.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Accessors(fluent = true, chain = true)
public enum PayloadStatusWithDefault {
    STARTED("S"),
    STOPPED("T"),
    UNKNOWN_DEFAULT_YOJO("UNKNOWN");

    private final String value;

    PayloadStatusWithDefault(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PayloadStatusWithDefault fromValue(String value) {
        for (PayloadStatusWithDefault v : PayloadStatusWithDefault.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        return UNKNOWN_DEFAULT_YOJO;
    }

}