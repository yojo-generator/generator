package slack.common;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import slack.common.AttachmentFields;

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