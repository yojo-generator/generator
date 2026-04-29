package slack.messages;

import slack.common.ConnectionErrorType;
import slack.common.ConnectionErrorError;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
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