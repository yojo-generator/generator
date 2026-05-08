package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.HelloType;

@Generated("Yojo")
/**
* First event received upon connection.
*/
public class Hello {


    private HelloType type;
    public void setType(HelloType type) {
        this.type = type;
    }
    public HelloType getType() {
        return type;
    }
}