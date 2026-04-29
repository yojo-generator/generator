package specFromIssue.common;

import javax.validation.constraints.NotBlank;
import javax.annotation.processing.Generated;

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
}