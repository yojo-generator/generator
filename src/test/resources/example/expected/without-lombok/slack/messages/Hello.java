package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.HelloType;

/**
* First event received upon connection.
*/
@Generated("Yojo")
public class Hello {


    private HelloType type;
    public void setType(HelloType type) {
        this.type = type;
    }
    public HelloType getType() {
        return type;
    }
}