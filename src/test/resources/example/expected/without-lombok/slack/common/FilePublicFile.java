package slack.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class FilePublicFile {


    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        return "FilePublicFile{" +
                "id=" + id +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePublicFile that = (FilePublicFile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}