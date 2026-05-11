package slack.common;

import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class BotChangedBot {


    private String id;

    private String appId;

    private String name;

    private Map<String, String> icons;
}