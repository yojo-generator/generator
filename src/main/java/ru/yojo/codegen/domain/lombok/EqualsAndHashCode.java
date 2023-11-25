package ru.yojo.codegen.domain.lombok;

public class EqualsAndHashCode {

    private boolean enable = false;
    private Boolean callSuper;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Boolean getCallSuper() {
        return callSuper;
    }

    public void setCallSuper(Boolean callSuper) {
        this.callSuper = callSuper;
    }

}
