package ru.yojo.codegen.domain;

@SuppressWarnings("all")
public final class LombokProperties {
    private final boolean enableLombok;
    private final boolean allArgsConstructor;

    private Accessors accessors;

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

    public static class Accessors {

        private boolean enable;
        private boolean fluent;
        private boolean chain;

        public Accessors(boolean enable, boolean fluent, boolean chain) {
            this.enable = enable;
            this.fluent = fluent;
            this.chain = chain;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isFluent() {
            return fluent;
        }

        public void setFluent(boolean fluent) {
            this.fluent = fluent;
        }

        public boolean isChain() {
            return chain;
        }

        public void setChain(boolean chain) {
            this.chain = chain;
        }
    }
}
