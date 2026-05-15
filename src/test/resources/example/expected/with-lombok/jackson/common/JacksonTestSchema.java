package jackson.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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
}