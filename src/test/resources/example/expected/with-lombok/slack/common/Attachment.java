package slack.common;

import java.util.List;
import slack.common.AttachmentFields;
import javax.validation.Valid;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import java.net.URI;
import java.math.BigDecimal;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
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

}