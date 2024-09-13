package ru.yojo.codegen.domain.lombok;

@SuppressWarnings("all")
public class LombokProperties {
    private boolean enableLombok;
    private boolean allArgsConstructor;
    private boolean noArgsConstructor = true;

    private Accessors accessors;
    private EqualsAndHashCode equalsAndHashCode;

    public LombokProperties() {
    }

    public static LombokProperties newLombokProperties(LombokProperties lombokProperties) {
        return new LombokProperties(lombokProperties.enableLombok, lombokProperties.allArgsConstructor, lombokProperties.getAccessors());
    }

    public EqualsAndHashCode getEqualsAndHashCode() {
        return equalsAndHashCode;
    }

    public void setEqualsAndHashCode(EqualsAndHashCode equalsAndHashCode) {
        this.equalsAndHashCode = equalsAndHashCode;
    }

    public LombokProperties(boolean enableLombok, boolean allArgsConstructor, Accessors accessors) {
        this.enableLombok = enableLombok;
        this.allArgsConstructor = allArgsConstructor;
        this.accessors = accessors;
    }

    public void setAccessors(Accessors accessors) {
        this.accessors = accessors;
    }

    public Accessors getAccessors() {
        return accessors;
    }

    public boolean enableLombok() {
        return enableLombok;
    }

    public void setEnableLombok(boolean enableLombok) {
        this.enableLombok = enableLombok;
    }

    public boolean allArgsConstructor() {
        return allArgsConstructor;
    }

    public void setAllArgsConstructor(boolean allArgsConstructor) {
        this.allArgsConstructor = allArgsConstructor;
    }

    public boolean noArgsConstructor() {
        return noArgsConstructor;
    }

    public void setNoArgsConstructor(boolean noArgsConstructor) {
        this.noArgsConstructor = noArgsConstructor;
    }
}
