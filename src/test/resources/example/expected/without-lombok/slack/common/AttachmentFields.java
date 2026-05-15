package slack.common;

import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class AttachmentFields {


    private String title;

    private String value;

    private Boolean shortField;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public void setShortField(Boolean shortField) {
        this.shortField = shortField;
    }
    public Boolean getShortField() {
        return shortField;
    }
    @Override
    public String toString() {
        return "AttachmentFields{" +
                "title=" + title + ", " +
                "value=" + value + ", " +
                "shortField=" + shortField +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentFields that = (AttachmentFields) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(value, that.value) &&
                Objects.equals(shortField, that.shortField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, value, shortField);
    }
}