package enumvalues.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public enum ErrorCode {
    VALIDATION_ERROR("VALIDATION_ERROR"),
    WALLET_NOT_FOUND("WALLET_NOT_FOUND"),
    INTERNAL_ERROR("INTERNAL_ERROR"),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ErrorCode fromValue(String value) {
        for (ErrorCode v : ErrorCode.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}