package ru.yojo.codegen.domain.message;

import ru.yojo.codegen.domain.FillParameters;

@SuppressWarnings("all")
public class MessageProperties {

    private String name;
    private String title;
    private String summary;
    private FillParameters fillParameters;

    public MessageProperties() {
    }

    public MessageProperties(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

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

    public FillParameters getPayload() {
        return fillParameters;
    }

    public void setPayload(FillParameters fillParameters) {
        this.fillParameters = fillParameters;
    }

    public String toWrite() {
        return fillParameters.toWrite();
    }
}
