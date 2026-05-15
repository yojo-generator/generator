package jackson.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JacksonTestSchema {


    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private OffsetDateTime createdAt;

    @JsonIgnore
    @JsonProperty("internal_code")
    private String internalCode;

    @JsonProperty("extra_data_custom")
    private String extraData;

    @JsonProperty("tags")
    private List<String> tags;
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }
    public String getInternalCode() {
        return internalCode;
    }
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
    public String getExtraData() {
        return extraData;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getTags() {
        return tags;
    }
    @Override
    public String toString() {
        return "JacksonTestSchema{" +
                "firstName=" + firstName + ", " +
                "createdAt=" + createdAt + ", " +
                "internalCode=" + internalCode + ", " +
                "extraData=" + extraData + ", " +
                "tags=" + tags +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JacksonTestSchema that = (JacksonTestSchema) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(internalCode, that.internalCode) &&
                Objects.equals(extraData, that.extraData) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, createdAt, internalCode, extraData, tags);
    }
}