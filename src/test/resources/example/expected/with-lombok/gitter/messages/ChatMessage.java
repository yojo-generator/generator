package gitter.messages;

import java.util.List;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import gitter.common.ChatMessageMentions;
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
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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

}