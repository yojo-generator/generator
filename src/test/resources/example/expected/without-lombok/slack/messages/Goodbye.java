package slack.messages;

import javax.annotation.processing.Generated;
import slack.common.GoodbyeType;

/**
* The server intends to close the connection soon.
*/
@Generated("Yojo")
public class Goodbye {


    private GoodbyeType type;
    public void setType(GoodbyeType type) {
        this.type = type;
    }
    public GoodbyeType getType() {
        return type;
    }
}