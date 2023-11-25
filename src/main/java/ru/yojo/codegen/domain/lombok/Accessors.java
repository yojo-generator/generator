package ru.yojo.codegen.domain.lombok;

public class Accessors {

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
