package example.testGenerate.slack.common;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Generated("Yojo")
public class GroupJoinedChannel {

    private String id;

    private String name;

    private BigDecimal created;

    private String creator;

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
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreator() {
        return creator;
    }
}