package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ConnectionErrorError;
import lombok.Data;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import example.testGenerate.slack.common.ConnectionErrorType;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
* Event received when a connection error happens.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class ConnectionError {

    private ConnectionErrorType type;

    @Valid
    private ConnectionErrorError error;

}