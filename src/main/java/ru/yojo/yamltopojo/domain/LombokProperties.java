package ru.yojo.yamltopojo.domain;

public final class LombokProperties {
    private final boolean enableLombok;
    private final boolean allArgsConstructor;
    private final boolean accessors;

    public LombokProperties(boolean enableLombok, boolean allArgsConstructor, boolean accessors) {
        this.enableLombok = enableLombok;
        this.allArgsConstructor = allArgsConstructor;
        this.accessors = accessors;
    }

    public boolean enableLombok() {
        return enableLombok;
    }

    public boolean allArgsConstructor() {
        return allArgsConstructor;
    }

    public boolean accessors() {
        return accessors;
    }
}
