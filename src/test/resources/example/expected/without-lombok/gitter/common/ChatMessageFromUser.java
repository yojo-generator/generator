package gitter.common;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;
import javax.annotation.processing.Generated;

/**
* User that sent the message.
*/
@Generated("Yojo")
public class ChatMessageFromUser {


    /**
     * Gitter User ID.
     */
    private String id;

    /**
     * Gitter/GitHub username.
     */
    private String username;

    /**
     * Gitter/GitHub user real name.
     */
    private String displayName;

    /**
     * Path to the user on Gitter.
     */
    private String url;

    /**
     * User avatar URI.
     */
    private URI avatarUrl;

    /**
     * User avatar URI (small).
     */
    private URI avatarUrlSmall;

    /**
     * User avatar URI (medium).
     */
    private URI avatarUrlMedium;

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
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    public void setAvatarUrl(URI avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public URI getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrlSmall(URI avatarUrlSmall) {
        this.avatarUrlSmall = avatarUrlSmall;
    }
    public URI getAvatarUrlSmall() {
        return avatarUrlSmall;
    }
    public void setAvatarUrlMedium(URI avatarUrlMedium) {
        this.avatarUrlMedium = avatarUrlMedium;
    }
    public URI getAvatarUrlMedium() {
        return avatarUrlMedium;
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
    @Override
    public String toString() {
        return "ChatMessageFromUser{" +
                "id=" + id + ", " +
                "username=" + username + ", " +
                "displayName=" + displayName + ", " +
                "url=" + url + ", " +
                "avatarUrl=" + avatarUrl + ", " +
                "avatarUrlSmall=" + avatarUrlSmall + ", " +
                "avatarUrlMedium=" + avatarUrlMedium + ", " +
                "v=" + v + ", " +
                "gv=" + gv +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageFromUser that = (ChatMessageFromUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(url, that.url) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(avatarUrlSmall, that.avatarUrlSmall) &&
                Objects.equals(avatarUrlMedium, that.avatarUrlMedium) &&
                Objects.equals(v, that.v) &&
                Objects.equals(gv, that.gv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, displayName, url, avatarUrl, avatarUrlSmall, avatarUrlMedium, v, gv);
    }
}