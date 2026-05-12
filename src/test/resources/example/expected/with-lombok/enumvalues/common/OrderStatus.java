package enumvalues.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Accessors(fluent = true, chain = true)
public enum OrderStatus {
    PENDING("P"),
    CONFIRMED("C"),
    CANCELLED("X");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        for (OrderStatus v : OrderStatus.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

}