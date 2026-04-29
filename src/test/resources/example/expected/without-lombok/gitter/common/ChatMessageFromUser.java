package example.testGenerate.gitter.common;

import javax.annotation.processing.Generated;
import java.net.URI;
import java.math.BigDecimal;

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
}