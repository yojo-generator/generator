package ru.yojo.codegen.domain.lombok;

/**
 * Represents Lombok's {@code @Accessors} configuration.
 * Controls fluent/chained setters and access style for generated DTOs.
 *
 * @author Vladimir Morozkin (TG @vmorozkin)
 */
public class Accessors {

    /**
     * Whether {@code @Accessors} should be applied at all.
     * If {@code false}, no accessor-related annotations are generated.
     */
    private boolean enable;

    /**
     * Enables fluent setters: {@code obj.setFoo(val) → obj.foo(val)}.
     */
    private boolean fluent;

    /**
     * Enables chained setters: {@code obj.setFoo(val) → returns this}.
     */
    private boolean chain;

    /**
     * Constructs an Accessors configuration.
     *
     * @param enable whether to enable {@code @Accessors}
     * @param fluent whether to generate fluent setters (no "set" prefix)
     * @param chain  whether setters should return {@code this} for chaining
     */
    public Accessors(boolean enable, boolean fluent, boolean chain) {
        this.enable = enable;
        this.fluent = fluent;
        this.chain = chain;
    }

    /**
     * Returns whether {@code @Accessors} annotation is enabled.
     *
     * @return {@code true} if accessors should be generated
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets whether {@code @Accessors} annotation is enabled.
     *
     * @param enable {@code true} to enable accessor customization
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Returns whether fluent setters (e.g., {@code foo(value)} instead of {@code setFoo(value)}) are enabled.
     *
     * @return {@code true} if fluent accessors are enabled
     */
    public boolean isFluent() {
        return fluent;
    }

    /**
     * Enables or disables fluent setters.
     *
     * @param fluent {@code true} to generate {@code foo(val)} instead of {@code setFoo(val)}
     */
    public void setFluent(boolean fluent) {
        this.fluent = fluent;
    }

    /**
     * Returns whether chained setters (returning {@code this}) are enabled.
     *
     * @return {@code true} if setters return {@code this} for method chaining
     */
    public boolean isChain() {
        return chain;
    }

    /**
     * Enables or disables chained setters.
     *
     * @param chain {@code true} to make setters return {@code this}
     */
    public void setChain(boolean chain) {
        this.chain = chain;
    }
}