package example.testGenerate.slack.common;

import java.util.List;
import example.testGenerate.slack.common.AttachmentFields;
import jakarta.validation.Valid;
import javax.annotation.processing.Generated;
import java.net.URI;
import java.math.BigDecimal;

@Generated("Yojo")
public class Attachment {

    private String fallback;

    private String color;

    private String pretext;

    private String authorName;

    private URI authorLink;

    private URI authorIcon;

    private String title;

    private URI titleLink;

    private String text;

    @Valid
    private List<AttachmentFields> fields;

    private URI imageUrl;

    private URI thumbUrl;

    private String footer;

    private URI footerIcon;

    private BigDecimal ts;

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }
    public String getFallback() {
        return fallback;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
    public void setPretext(String pretext) {
        this.pretext = pretext;
    }
    public String getPretext() {
        return pretext;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorLink(URI authorLink) {
        this.authorLink = authorLink;
    }
    public URI getAuthorLink() {
        return authorLink;
    }
    public void setAuthorIcon(URI authorIcon) {
        this.authorIcon = authorIcon;
    }
    public URI getAuthorIcon() {
        return authorIcon;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setTitleLink(URI titleLink) {
        this.titleLink = titleLink;
    }
    public URI getTitleLink() {
        return titleLink;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setFields(List<AttachmentFields> fields) {
        this.fields = fields;
    }
    public List<AttachmentFields> getFields() {
        return fields;
    }
    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }
    public URI getImageUrl() {
        return imageUrl;
    }
    public void setThumbUrl(URI thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public URI getThumbUrl() {
        return thumbUrl;
    }
    public void setFooter(String footer) {
        this.footer = footer;
    }
    public String getFooter() {
        return footer;
    }
    public void setFooterIcon(URI footerIcon) {
        this.footerIcon = footerIcon;
    }
    public URI getFooterIcon() {
        return footerIcon;
    }
    public void setTs(BigDecimal ts) {
        this.ts = ts;
    }
    public BigDecimal getTs() {
        return ts;
    }
}