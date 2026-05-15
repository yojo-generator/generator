package slack.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

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
    @Override
    public String toString() {
        return "GroupRenameChannel{" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "created=" + created +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupRenameChannel that = (GroupRenameChannel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created);
    }
}