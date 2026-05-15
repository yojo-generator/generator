package slack.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class FileCommentDeletedFile {


    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        return "FileCommentDeletedFile{" +
                "id=" + id +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileCommentDeletedFile that = (FileCommentDeletedFile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}