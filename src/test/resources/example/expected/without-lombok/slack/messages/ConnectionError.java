package example.testGenerate.slack.messages;

import example.testGenerate.slack.common.ConnectionErrorError;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import example.testGenerate.slack.common.ConnectionErrorType;

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