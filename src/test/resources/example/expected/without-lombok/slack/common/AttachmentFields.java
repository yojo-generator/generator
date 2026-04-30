package slack.common;

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
}