package ru.yojo.codegen.domain;

@SuppressWarnings("all")
public class MessageProperties {

    private String name;
    private String title;
    private String summary;
    private MessagePayload payload;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public MessagePayload getPayload() {
        return payload;
    }

    public void setPayload(MessagePayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return payload.toString();
    }
}
