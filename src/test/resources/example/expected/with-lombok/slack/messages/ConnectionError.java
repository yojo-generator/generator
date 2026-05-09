package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.ConnectionErrorError;
import slack.common.ConnectionErrorType;

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