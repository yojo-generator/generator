package example.testGenerate.gitter.common;

import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.math.BigDecimal;

/**
* User that sent the message.
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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

}