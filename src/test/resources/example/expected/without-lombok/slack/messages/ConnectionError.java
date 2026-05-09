package slack.messages;

import javax.annotation.processing.Generated;
import javax.validation.Valid;
import slack.common.ConnectionErrorError;
import slack.common.ConnectionErrorType;

/**
* Event received when a connection error happens.
*/
@Generated("Yojo")
public class ConnectionError {


    private ConnectionErrorType type;

    @Valid
    private ConnectionErrorError error;
    public void setType(ConnectionErrorType type) {
        this.type = type;
    }
    public ConnectionErrorType getType() {
        return type;
    }
    public void setError(ConnectionErrorError error) {
        this.error = error;
    }
    public ConnectionErrorError getError() {
        return error;
    }
}