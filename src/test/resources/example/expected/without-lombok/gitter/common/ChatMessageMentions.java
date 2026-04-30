package gitter.common;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ChatMessageMentions {

    private String screenName;

    private String userId;

    private List<Object> userIds;

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    public String getScreenName() {
        return screenName;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserIds(List<Object> userIds) {
        this.userIds = userIds;
    }
    public List<Object> getUserIds() {
        return userIds;
    }
}