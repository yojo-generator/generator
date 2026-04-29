package example.testGenerate.slack.common;

import java.util.Map;
import lombok.Data;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class BotAddedBot {

    private String id;

    private String appId;

    private String name;

    private Map<String, String> icons;

}