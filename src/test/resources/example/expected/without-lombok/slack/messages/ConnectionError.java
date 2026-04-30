package slack.messages;

import slack.common.ConnectionErrorType;
import slack.common.ConnectionErrorError;
import javax.validation.Valid;
import javax.annotation.processing.Generated;

/**
* Event received when a connection error happens.
*/
@Generated("Yojo")
public class ConnectionError {

    private ConnectionErrorType type;

    @Valid
    private ConnectionErrorError error;

    public void setError(ConnectionErrorError error) {
        this.error = error;
    }
    public ConnectionErrorError getError() {
        return error;
    }
}