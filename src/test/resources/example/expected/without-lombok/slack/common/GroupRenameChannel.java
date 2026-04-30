package slack.common;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Generated("Yojo")
public class GroupRenameChannel {

    private String id;

    private String name;

    private BigDecimal created;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setCreated(BigDecimal created) {
        this.created = created;
    }
    public BigDecimal getCreated() {
        return created;
    }
}