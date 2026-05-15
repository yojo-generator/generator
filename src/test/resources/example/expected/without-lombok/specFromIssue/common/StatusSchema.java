package specFromIssue.common;

import java.util.Objects;
import javax.annotation.processing.Generated;
import javax.validation.constraints.NotBlank;

@Generated("Yojo")
public class StatusSchema {


    @NotBlank
    private String code;

    @NotBlank
    private String description;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return "StatusSchema{" +
                "code=" + code + ", " +
                "description=" + description +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusSchema that = (StatusSchema) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }
}