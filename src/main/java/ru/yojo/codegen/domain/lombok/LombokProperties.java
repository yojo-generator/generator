package ru.yojo.codegen.domain.lombok;

@SuppressWarnings("all")
@lombok.experimental.Accessors(chain = true)
public class LombokProperties {
    private boolean enableLombok;
    private boolean allArgsConstructor;

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

    public boolean allArgsConstructor() {
        return allArgsConstructor;
    }
}
