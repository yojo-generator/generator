package gitter.common;

import java.math.BigDecimal;
import java.net.URI;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
/**
* User that sent the message.
*/
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