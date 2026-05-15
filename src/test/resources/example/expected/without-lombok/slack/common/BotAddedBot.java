package slack.common;

import java.util.Map;
import java.util.Objects;
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
    @Override
    public String toString() {
        return "BotAddedBot{" +
                "id=" + id + ", " +
                "appId=" + appId + ", " +
                "name=" + name + ", " +
                "icons=" + icons +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotAddedBot that = (BotAddedBot) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(appId, that.appId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(icons, that.icons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, name, icons);
    }
}