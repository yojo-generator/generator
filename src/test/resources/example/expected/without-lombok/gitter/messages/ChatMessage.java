package gitter.messages;

import gitter.common.ChatMessageMentions;
import java.util.List;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import gitter.common.ChatMessageFromUser;
import gitter.common.ChatMessageIssues;
import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.net.URI;

/**
* A message represents an individual chat message sent to a room. They are a sub-resource of a room.
*/
@Generated("Yojo")
public class ChatMessage {

    /**
     * ID of the message.
     */
    private String id;

    /**
     * Original message in plain-text/markdown.
     */
    private String text;

    /**
     * HTML formatted message.
     */
    private String html;

    /**
     * ISO formatted date of the message.
     */
    private OffsetDateTime sent;

    /**
     * User that sent the message.
     */
    @Valid
    private ChatMessageFromUser fromUser;

    /**
     * Boolean that indicates if the current user has read the message.
     */
    private Boolean unread;

    /**
     * Number of users that have read the message.
     */
    private BigDecimal readBy;

    /**
     * List of URLs present in the message.
     */
    private List<URI> urls;

    /**
     * List of @Mentions in the message.
     */
    @Valid
    private List<ChatMessageMentions> mentions;

    /**
     * List of #Issues referenced in the message.
     */
    @Valid
    private List<ChatMessageIssues> issues;

    /**
     * Metadata. This is currently not used for anything.
     */
    private List<Object> meta;

    /**
     * Version.
     */
    private BigDecimal v;

    /**
     * Stands for "Gravatar version" and is used for cache busting.
     */
    private String gv;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setHtml(String html) {
        this.html = html;
    }
    public String getHtml() {
        return html;
    }
    public void setSent(OffsetDateTime sent) {
        this.sent = sent;
    }
    public OffsetDateTime getSent() {
        return sent;
    }
    public void setFromUser(ChatMessageFromUser fromUser) {
        this.fromUser = fromUser;
    }
    public ChatMessageFromUser getFromUser() {
        return fromUser;
    }
    public void setUnread(Boolean unread) {
        this.unread = unread;
    }
    public Boolean getUnread() {
        return unread;
    }
    public void setReadBy(BigDecimal readBy) {
        this.readBy = readBy;
    }
    public BigDecimal getReadBy() {
        return readBy;
    }
    public void setUrls(List<URI> urls) {
        this.urls = urls;
    }
    public List<URI> getUrls() {
        return urls;
    }
    public void setMentions(List<ChatMessageMentions> mentions) {
        this.mentions = mentions;
    }
    public List<ChatMessageMentions> getMentions() {
        return mentions;
    }
    public void setIssues(List<ChatMessageIssues> issues) {
        this.issues = issues;
    }
    public List<ChatMessageIssues> getIssues() {
        return issues;
    }
    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }
    public List<Object> getMeta() {
        return meta;
    }
    public void setV(BigDecimal v) {
        this.v = v;
    }
    public BigDecimal getV() {
        return v;
    }
    public void setGv(String gv) {
        this.gv = gv;
    }
    public String getGv() {
        return gv;
    }
}