package example.testGenerate.slack.common;

import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class BotAddedBot {

    private String id;

    private String appId;

    private String name;

    private Map<String, String> icons;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getAppId() {
        return appId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setIcons(Map<String, String> icons) {
        this.icons = icons;
    }
    public Map<String, String> getIcons() {
        return icons;
    }
}