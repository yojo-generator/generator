package slack.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.processing.Generated;

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
    @Override
    public String toString() {
        return "GroupJoinedChannel{" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "created=" + created + ", " +
                "creator=" + creator +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupJoinedChannel that = (GroupJoinedChannel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(created, that.created) &&
                Objects.equals(creator, that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created, creator);
    }
}