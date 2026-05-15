package gitter.common;

import java.util.List;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("Yojo")
public class ChatMessageMentions {


    private String screenName;

    private String userId;

    private List<String> userIds;
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
    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    public List<String> getUserIds() {
        return userIds;
    }
    @Override
    public String toString() {
        return "ChatMessageMentions{" +
                "screenName=" + screenName + ", " +
                "userId=" + userId + ", " +
                "userIds=" + userIds +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageMentions that = (ChatMessageMentions) o;
        return Objects.equals(screenName, that.screenName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(userIds, that.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenName, userId, userIds);
    }
}